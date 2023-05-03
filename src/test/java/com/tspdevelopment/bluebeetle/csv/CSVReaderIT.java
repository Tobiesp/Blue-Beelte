package com.tspdevelopment.bluebeetle.csv;

import com.tspdevelopment.bluebeetle.BlueBeetleApplication;
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
@SpringBootTest(classes = BlueBeetleApplication.class)
@ActiveProfiles("test")
public class CSVReaderIT {
    
    private String sample = null;
    
    public CSVReaderIT() {
//        try {
//            Reader reader = new FileReader(new File("C:\\Users\\tobiesp\\Documents\\TeamKids Export\\Students.csv"));
//            StringBuilder sb = new StringBuilder();
//            int i = 0;
//            while(i > -1) {
//                i = reader.read();
//                sb.append((char)i);
//            }
//            sample = sb.toString();
        sample = "\"stringValue\",\"intValue\",doubleValue,\"boolValue\"\n\"test\",1,1.1,True";
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(CSVReaderIT.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(CSVReaderIT.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    private TestObject getTestObject() {
        TestObject result = new TestObject();
        result.setBoolValue(true);
        result.setDoubleValue(1.1);
        result.setIntValue(1);
        result.setStringValue("test");
        return result;
    }

    /**
     * Test of readItemRow method, of class CSVReader.
     */
    @Test
    public void testReadItemRow_Class() throws Exception {
        System.out.println("readItemRow");
        StringReader reader = new StringReader(sample);
        CSVReader instance = new CSVReader(reader, CSVPreference.STANDARD_PREFERENCE);
        Object expResult = getTestObject();
        Object result = instance.readItemRow(TestObject.class);
        assertEquals(expResult, result);
    }

    /**
     * Test of readItemRow method, of class CSVReader.
     */
    @Test
    public void testReadItemRow_Class_StringArr() throws Exception {
        System.out.println("readItemRow");
        StringReader reader = new StringReader(sample);
        CSVReader instance = new CSVReader(reader, CSVPreference.STANDARD_PREFERENCE);
        Object expResult = getTestObject();
        Object result = instance.readItemRow(TestObject.class, "stringValue", "intValue", "doubleValue", "boolValue");
        assertEquals(expResult, result);
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
//        List<String> expResult = Arrays.asList(new String[]{"Student_Name", "Group", "Grade", "Graduated"});
        List<String> result = instance.getHeaders();
        assertArrayEquals(expResult.toArray(), result.toArray());
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
