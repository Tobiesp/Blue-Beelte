package com.tspdevelopment.bluebeetle.controller;

import com.tspdevelopment.bluebeetle.views.AuthRequest;
import com.tspdevelopment.bluebeetle.response.UserLoginView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author tobiesp
 */
public class AuthControllerIT {
    
    public AuthControllerIT() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }

    /**
     * Test of login method, of class AuthController.
     */
    @Test
    public void testLogin() {
        System.out.println("login");
        AuthRequest request = null;
        AuthController instance = null;
        ResponseEntity<UserLoginView> expResult = null;
        ResponseEntity<UserLoginView> result = instance.login(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of logout method, of class AuthController.
     */
    @Test
    public void testLogout() {
        System.out.println("logout");
        HttpHeaders headers = null;
        AuthController instance = null;
        ResponseEntity<?> expResult = null;
        ResponseEntity<?> result = instance.logout(headers);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
