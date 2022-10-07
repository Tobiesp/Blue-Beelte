package com.tspdevelopment.kidsscore.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tobiesp
 */
public class CSVReader {
    
    private final Reader reader;
    private final CSVPreference preference;
    private int rowIndex;
    private int lastRead;
    
    public CSVReader(Reader reader, CSVPreference preference) {
        this.reader = reader;
        this.preference = preference;
        this.rowIndex = 0;
    }
    
    public <T> T readIRow(T item, String... map) {
        
        return null;
    }
    
    public boolean hasRow() {
        return this.lastRead != -1;
    }
    
    public List<String> readRow() throws IOException {
        List<String> list = new ArrayList();
        boolean endOfLine = false;
        StringBuilder sb = new StringBuilder();
        char c;
        if(this.preference.hasHeader() && (this.rowIndex == 0)) {
            while(!endOfLine) {
                c = (char) reader.read();
                this.lastRead = c;
                if(c == -1) {
                    return list;
                }
                sb.append(c);
                if (sb.toString().endsWith(preference.getEndOfLineSymbols())) {
                    endOfLine = true;
                    sb = new StringBuilder();
                    this.rowIndex += 1;
                }
            }
        }
        endOfLine = false;
        
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
        String[] array = sb.toString().split(",");
        String value = null;
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
    
    
    
}
