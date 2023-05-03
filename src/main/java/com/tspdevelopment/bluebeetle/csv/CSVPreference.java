package com.tspdevelopment.bluebeetle.csv;

/**
 *
 * @author tobiesp
 */
public class CSVPreference {
    public static CSVPreference STANDARD_PREFERENCE = new CSVPreference('"', ',', "\n", false, true);
    
    private final char quoteChar;
    private final char delimiterChar;
    private final String endOfLineSymbols;
    private final boolean surroundingSpacesNeedQuotes;
    private final boolean header;
    
    public CSVPreference(char quoteChar, char delimiterChar, String endOfLineSymbols, boolean surroundingSpacesNeedQuotes, boolean header) {
        this.quoteChar = quoteChar;
        this.delimiterChar = delimiterChar;
        this.endOfLineSymbols = endOfLineSymbols;
        this.surroundingSpacesNeedQuotes = surroundingSpacesNeedQuotes;
        this.header = header;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    public char getDelimiterChar() {
        return delimiterChar;
    }

    public String getEndOfLineSymbols() {
        return endOfLineSymbols;
    }

    public boolean isSurroundingSpacesNeedQuotes() {
        return surroundingSpacesNeedQuotes;
    }

    public boolean hasHeader() {
        return header;
    }
    
}
