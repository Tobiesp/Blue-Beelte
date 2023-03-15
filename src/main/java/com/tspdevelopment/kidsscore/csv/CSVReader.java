package com.tspdevelopment.kidsscore.csv;

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
            System.out.println("Row data lenght: " + rowData.size());
            Field[] fields = item.getClass().getDeclaredFields();
            for(Field f : fields) {
                f.setAccessible(true);
                String fieldName = f.getName().trim().toLowerCase();
                System.out.println("Field Name: " + fieldName);
                for(int i=0;i<map.length;i++) {
                    String header = map[i];
                    System.out.println("Map Name: " + fieldName + "-" + header.trim().toLowerCase());
                    if(fieldName.equals(header.trim().toLowerCase())) {
                        Object value = getValue(rowData.get(i), f.getType());
                        System.out.println("Value: " + String.valueOf(value));
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
                System.out.println("Line EOF: " + sb.toString());
                this.rowIndex += 1;
                break;
            }
            sb.append(c);
            if (sb.toString().endsWith(preference.getEndOfLineSymbols())) {
                System.out.println("Line NL: " + sb.toString());
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
        for(char c : str.toCharArray()) {
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
                System.out.println("String value: " + value.toString());
                list.add(value.toString());
                value = new StringBuilder();
                continue;
            }
            value.append(c);
        }
        if(value.length() > 0) {
            System.out.println("Last String value: " + value.toString());
            list.add(value.toString());
        }
        return list;
    }

    private Object getValue(String value, Class<?> clazz) {
        System.out.println("Value Class: " + clazz.getName());
        if(clazz.equals(int.class) || clazz.equals(Integer.class)) {
            return Integer.valueOf(value);
        } else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
            return Double.valueOf(value);
        } else if(clazz.equals(Float.class) || clazz.equals(float.class)) {
            return Float.valueOf(value);
        } else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
            return Long.valueOf(value);
        } else if(clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return ((value.toLowerCase().charAt(0) == 't') || (value.toLowerCase().charAt(0) == 'y'));
        } else if(clazz.equals(String.class)) {
            return value;
        } else {
            return null;
        }
    }
    
    
    
}
