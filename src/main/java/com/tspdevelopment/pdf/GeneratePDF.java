package com.tspdevelopment.pdf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneratePDF {
    
    private GeneratePDF(){

    }
    
    public static GeneratePDF getInstance() {
        return GeneratePDFHolder.INSTANCE;
    }

    public ByteArrayOutputStream generateTestPDF() throws DocumentException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, stream);

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk("Hello World", font);

        document.add(chunk);
        document.close();

        return stream;
    }
    
    private static class GeneratePDFHolder {
        private static final GeneratePDF INSTANCE = new GeneratePDF();
    }
}
