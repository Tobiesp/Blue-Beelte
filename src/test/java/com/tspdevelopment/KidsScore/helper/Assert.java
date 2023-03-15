/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.KidsScore.helper;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author tobiesp
 */
public class Assert {
    
    public static void assertEqualsList(List a, List b) {
        if(a.size() == b.size()) {
            for(int i=0; i<a.size(); i++) {
                assertEquals(a.get(i), b.get(i));
            }
        }
    }
    
}
