package com.tspdevelopment.bluebeetle.csv;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tobiesp
 */
public class CSVReader {
    
    private static final Logger LOGGER = Logger.getLogger(CSVReader.class.getName());
    
    private final Reader reader;
    private final CSVPreference preference;
    private int rowIndex;
    private int lastRead;
    private List<String> headers;
    
    public CSVReader(Reader reader, CSVPreference preference) {
        this.reader = reader;
        this.preference = preference;
        this.rowIndex = 0;
        this.headers = new ArrayList<>();
    }
    
    public <T> T readItemRow(Class<T> clazz) throws IOException {
        if(headers.isEmpty() && this.preference.hasHeader()) {
            this.getHeaders();
            if(headers.isEmpty()) {
                throw new NullPointerException("Header can not be empty.");
            }
        }
        return this.readItemRow(clazz, headers.toArray(String[]::new));
    }
    
    public <T> T readItemRow(Class<T> clazz, String... map) throws IOException {
        try {
            Object item = clazz.getConstructors()[0].newInstance();
            List<String> rowData = readRow();
            LOGGER.log(Level.FINEST, "Row data lenght: {0}", rowData.size());
            Field[] fields = item.getClass().getDeclaredFields();
            for(Field f : fields) {
                f.setAccessible(true);
                String fieldName = f.getName().trim().toLowerCase();
                LOGGER.log(Level.FINEST, "Field Name: {0}", fieldName);
                for(int i=0;i<map.length;i++) {
                    if(i >= rowData.size()) {
                        continue;
                    }
                    String header = map[i];
                    LOGGER.log(Level.INFO, "Map Name: {0}-{1}", new Object[]{fieldName, header.trim().toLowerCase()});
                    if(fieldName.equals(header.trim().toLowerCase())) {
                        Object value = getValue(rowData.get(i), f.getType());
                        LOGGER.log(Level.INFO, "Value: {0}", String.valueOf(value));
                        f.set(item, value);
                    }
                }
            }
            return (T) item;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.log(Level.SEVERE, "Error reading CSV file row", ex);
        }
        return null;
    }
    
    public boolean hasRow() {
        return this.lastRead != 65535;
    }
    
    public List<String> getHeaders() {
        try {
            readHeaders();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to read headers", ex);
        }
        return this.headers;
    }
    
    public List<String> readRow() throws IOException {
        readHeaders();
        return proccessString(readLine());
    }
    
    private void readHeaders() throws IOException {
        if(headers.isEmpty() && (this.rowIndex == 0) && this.preference.hasHeader()) {
            headers = proccessString(readLine());
        }
    }
    
    private String readLine() throws IOException {
        boolean endOfLine = false;
        StringBuilder sb = new StringBuilder();
        char c;
        while(!endOfLine) {
            c = (char) reader.read();
            this.lastRead = c;
            if((int)c == 65535) {
                LOGGER.log(Level.INFO, "Line EOF: {0}", sb.toString());
                this.rowIndex += 1;
                break;
            }
            sb.append(c);
            if (sb.toString().endsWith(preference.getEndOfLineSymbols())) {
                LOGGER.log(Level.INFO, "Line NL: {0}", sb.toString());
                this.rowIndex += 1;
                break;
            }
        }
        return sb.toString().replace(preference.getEndOfLineSymbols(), "");
    }
    
    private List<String> proccessString(String str) {
        List<String> list = new ArrayList();
        StringBuilder value = new StringBuilder();
        boolean quote = false;
        boolean first = true;
        for(char c : str.toCharArray()) {
            if(first && !validSymbol(c)){
                continue;
            } 
            first = false;
            if((c == '"') && !quote) {
                quote = true;
                continue;
            }
            if((c == '"') && quote) {
                quote = false;
                continue;
            }
            if(quote) {
                value.append(c);
                continue;
            }
            if((c == ',') && !quote) {
                LOGGER.log(Level.INFO, "String value: {0}", value.toString());
                list.add(value.toString());
                value = new StringBuilder();
                continue;
            }
            value.append(c);
        }
        if(value.length() > 0) {
            LOGGER.log(Level.INFO, "Last String value: {0}", value.toString());
            list.add(value.toString());
        }
        return list;
    }
    
    private boolean validSymbol(char c) {
        return Character.isLetterOrDigit(c) || Character.isWhitespace(c);
    }

    private Object getValue(String value, Class<?> clazz) {
        LOGGER.log(Level.INFO, "Value Class: {0}", clazz.getName());
        if(clazz.equals(int.class) || clazz.equals(Integer.class)) {
            return Integer.valueOf(value.trim());
        } else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
            return Double.valueOf(value.trim());
        } else if(clazz.equals(Float.class) || clazz.equals(float.class)) {
            return Float.valueOf(value.trim());
        } else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
            return Long.valueOf(value.trim());
        } else if(clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return ((value.toLowerCase().charAt(0) == 't') || (value.toLowerCase().charAt(0) == 'y'));
        } else if(clazz.equals(String.class)) {
            return value;
        } else {
            return null;
        }
    }
    
    
    
}
