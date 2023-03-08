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
        try {
            Object item = clazz.getConstructors()[0].newInstance();
            List<String> rowData = readRow();
            Field[] fields = item.getClass().getDeclaredFields();
            for(Field f : fields) {
                f.setAccessible(true);
                String fieldName = f.getName().trim().toLowerCase();
                for(int i=0;i<headers.size();i++) {
                    String header = headers.get(i);
                    if(fieldName.equals(header.trim().toLowerCase())) {
                        Object value = getValue(rowData.get(i), f.getDeclaringClass());
                        f.set(item, value);
                    }
                }
            }
            return (T) item;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, "Error reading CSV file row", ex);
        }
        return null;
    }
    
    public <T> T readItemRow(Class<T> clazz, String... map) throws IOException {
        try {
            Object item = clazz.getConstructors()[0].newInstance();
            List<String> rowData = readRow();
            Field[] fields = item.getClass().getDeclaredFields();
            for(Field f : fields) {
                f.setAccessible(true);
                String fieldName = f.getName().trim().toLowerCase();
                for(int i=0;i<map.length;i++) {
                    String header = map[i];
                    if(fieldName.equals(header.trim().toLowerCase())) {
                        Object value = getValue(rowData.get(i), f.getDeclaringClass());
                        f.set(item, value);
                    }
                }
            }
            return (T) item;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, "Error reading CSV file row", ex);
        }
        return null;
    }
    
    public boolean hasRow() {
        return this.lastRead != -1;
    }
    
    public List<String> getHeaders() throws IOException {
        if(headers.isEmpty() && (this.rowIndex == 0) && this.preference.hasHeader()) {
            headers = readRow();
            return headers;
        } else {
            return this.headers;
        }
    }
    
    public List<String> readRow() throws IOException {
        boolean endOfLine = false;
        StringBuilder sb = new StringBuilder();
        char c;
        
        while(!endOfLine) {
            c = (char) reader.read();
            this.lastRead = c;
            if(c == -1) {
                break;
            }
            sb.append(c);
            if (sb.toString().endsWith(preference.getEndOfLineSymbols())) {
                endOfLine = true;
                this.rowIndex += 1;
            }
        }
        return proccessString(sb.toString());
    }
    
    private List<String> proccessString(String str) {
        List<String> list = new ArrayList();
        String value = null;
        String[] array = str.split(",");
        for(String s : array) {
            if(s.endsWith("\"") && value != null) {
                value += "," + s;
                list.add(value);
                value = null;
            } else if(value != null) {
                value += "," + s;
            }  else if(s.startsWith("\"") && value == null) {
                value = s;
            } else {
                list.add(s);
                value = null;
            }
        }
        return list;
    }

    private Object getValue(String value, Class<?> clazz) {
        if(clazz.equals(int.class) || clazz.equals(Integer.class)) {
            return Integer.valueOf(value);
        } else if(clazz.equals(Double.class)) {
            return Double.valueOf(value);
        } else if(clazz.equals(Float.class)) {
            return Float.valueOf(value);
        } else if(clazz.equals(Long.class)) {
            return Long.valueOf(value);
        } else if(clazz.equals(Boolean.class)) {
            return ((value.toLowerCase().charAt(0) == 't') || (value.toLowerCase().charAt(0) == 'y'));
        } else if(clazz.equals(String.class)) {
            return value;
        } else {
            return null;
        }
    }
    
    
    
}
