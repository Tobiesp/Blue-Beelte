/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.tspdevelopment.KidsScore.csv;

import com.tspdevelopment.kidsscore.KidsScoreApplication;
import com.tspdevelopment.kidsscore.csv.CSVPreference;
import com.tspdevelopment.kidsscore.csv.CSVReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author tobiesp
 */
@SpringBootTest(classes = KidsScoreApplication.class)
@ActiveProfiles("test")
public class CSVReaderIT {
    
    private final String sample;
    
    public CSVReaderIT() {
        sample = "\"stringValue\",\"intValue\",\"doubleValue\",\"boolValue\"\n\"test\",\"1\",\"1.1\",\"True\"";
    }
    
    @BeforeAll
    public static void setUpClass() {
    }

    /**
     * Test of readItemRow method, of class CSVReader.
     */
    @Test
    public void testReadItemRow_Class() throws Exception {
        System.out.println("readItemRow");
        StringReader reader = new StringReader(sample);
        CSVReader instance = new CSVReader(reader, CSVPreference.STANDARD_PREFERENCE);
        Object expResult = new TestObject("test", 1, 1.1, true);
        Object result = instance.readItemRow(TestObject.class);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readItemRow method, of class CSVReader.
     */
    @Test
    public void testReadItemRow_Class_StringArr() throws Exception {
        System.out.println("readItemRow");
        StringReader reader = new StringReader(sample);
        CSVReader instance = new CSVReader(reader, CSVPreference.STANDARD_PREFERENCE);
        Object expResult = new TestObject("test", 1, 1.1, true);
        Object result = instance.readItemRow(TestObject.class, "stringValue", "intValue", "doubleValue", "boolValue");
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasRow method, of class CSVReader.
     */
    @Test
    public void testHasRow() {
        System.out.println("hasRow");
        StringReader reader = new StringReader(sample);
        CSVReader instance = new CSVReader(reader, CSVPreference.STANDARD_PREFERENCE);
        boolean expResult = true;
        boolean result = instance.hasRow();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHeaders method, of class CSVReader.
     */
    @Test
    public void testGetHeaders() throws Exception {
        System.out.println("getHeaders");
        StringReader reader = new StringReader(sample);
        CSVReader instance = new CSVReader(reader, CSVPreference.STANDARD_PREFERENCE);
        List<String> expResult = Arrays.asList(new String[]{"stringValue", "intValue", "doubleValue", "boolValue"});
        List<String> result = instance.getHeaders();
        assertEquals(expResult, result);
    }

    /**
     * Test of readRow method, of class CSVReader.
     */
    @Test
    public void testReadRow() throws Exception {
        System.out.println("readRow");
        StringReader reader = new StringReader(sample);
        CSVReader instance = new CSVReader(reader, CSVPreference.STANDARD_PREFERENCE);
        List<String> expResult = Arrays.asList(new String[]{"test", "1", "1.1", "True"});
        List<String> result = instance.readRow();
        assertEquals(expResult, result);
    }
    
}
