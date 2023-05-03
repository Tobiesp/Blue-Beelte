package com.tspdevelopment.bluebeetle.csv;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

/**
 *
 * @author tobiesp
 */
public class CSVWriter {
    private final Writer writer;
    private final CSVPreference preference;
    
    public CSVWriter(Writer writer, CSVPreference preference) {
        this.writer = writer;
        this.preference = preference;
    }
    
    private String processString(String s) {
        if(s.contains(",") || (s.matches("\\S+") && this.preference.isSurroundingSpacesNeedQuotes())) {
            return this.preference.getQuoteChar() + s + this.preference.getQuoteChar();
        } else {
            return s;
        }
    }
    
    private String findValueInObject(Object o, String s) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Object r = null;
        if(s.contains(":")) {
            String[] array = s.split(":");
            for(String m : array) {
                r = findItem(o, m);
            }
        } else {
            r = findItem(o, s);
        }
        return String.valueOf(r);
    }
    
    private Object findItem(Object o, String s) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class c = o.getClass();
        Field f = c.getDeclaredField(s);
        return f.get(o);
    }
    
    public void writeHeader(String... headers) throws IOException {
        if(headers.length > 0) {
            boolean first = true;
            for(String s: headers) {
                if(first) {
                    this.writer.write(processString(s));
                    first = false;
                } else {
                    this.writer.write(this.preference.getDelimiterChar());
                    this.writer.write(processString(s));
                }
            }
        }
    }
    
    public void write(String... row) throws IOException {
        boolean first = true;
        for(String s: row) {
            if(first) {
                this.writer.write(processString(s));
                first = false;
            } else {
                this.writer.write(this.preference.getDelimiterChar());
                this.writer.write(processString(s));
            }
        }
        this.writer.write(this.preference.getEndOfLineSymbols());
    }
    
    public void write(Object o, String... map) throws IOException, NoSuchFieldException {
        String[] array = new String[map.length];
        String m;
        for(int i=0; i<map.length; i++) {
            m = map[i];
            try {
                array[i] = findValueInObject(o, m);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new IOException("Unable to access the object field.", ex);
            }
        }
        this.write(array);
    }
    
}
