/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.bluebeetle.helpers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tspdevelopment.bluebeetle.data.model.User;
import java.util.UUID;

/**
 *
 * @author tobiesp
 */
public class SecurityHelper {

    private final String VALID_CHAR = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
    private final int MAX_FAILED_LOGIN_COUNT = 3;
    
    private SecurityHelper() {
    }

    public String generateSecret() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 25; i++) {
            int r = (int) (Math.random() * (VALID_CHAR.length()-1));
            sb.append(VALID_CHAR.charAt(r));
        }
        return sb.toString();
    }

    public String generateSecretId() {
        return UUID.randomUUID().toString();
    }
    
    public static SecurityHelper getInstance() {
        return SecurityHelperHolder.INSTANCE;
    }
    
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public boolean validUser(User user) {
        if(user.getFailedAttempt() > MAX_FAILED_LOGIN_COUNT) {
            return false;
        }
        if(!user.isAccountNonExpired()) {
            return false;
        }
        if(!user.isEnabled()) {
            return false;
        }
        return true;
    }
    
    public String encodePassword(String password){
        PasswordEncoder encoder = passwordEncoder();
        return encoder.encode(password);
    }
    
    public boolean validPassword(String password) {
        //Password rules:
        //1. Lenght must be greater then 8
        //2. Must have at least one Captital leter
        //3. Must have at least one number
        //4. Must have at least one of the following symbols: !#$%^&* _-.?
        if(password == null) {
            return false;
        } else if(password.length() < 8) {
            return false;
        }
        char ch;
        boolean capitalFlag = false;
        boolean symbolFlag = false;
        boolean numberFlag = false;
        for(int i=0; i<password.length(); i++) {
             ch = password.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            } else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            }
            switch(ch){
                case '!':
                    symbolFlag = true;
                    break;
                case '#':
                    symbolFlag = true;
                    break;
                case '$':
                    symbolFlag = true;
                    break;
                case '%':
                    symbolFlag = true;
                    break;
                case '^':
                    symbolFlag = true;
                    break;
                case '&':
                    symbolFlag = true;
                    break;
                case '*':
                    symbolFlag = true;
                    break;
                case '-':
                    symbolFlag = true;
                    break;
                case '_':
                    symbolFlag = true;
                    break;
                case ' ':
                    symbolFlag = true;
                    break;
                case '.':
                    symbolFlag = true;
                    break;
                case '?':
                    symbolFlag = true;
                    break;
            }
        }
        return capitalFlag && symbolFlag && numberFlag;
    }
    
    public long processExperationTime(String time) {
        long t = 1l;
        //Validate string
        //Valid chars y,M,w,d,h,m
        //Seperate the number
        StringBuilder number = new StringBuilder();
        StringBuilder value = new StringBuilder();
        for(int i=0;i<time.length(); i++) {
            char c = time.charAt(i);
            if(Character.isDigit(c)) {
                number.append(c);
            }
            if(!Character.isDigit(c)) {
                value.append(c);
            }
        }
        if(!number.toString().isEmpty()) {
            t = t*Long.parseLong(number.toString());
        }
        switch(value.toString()){
            case "y":
                t = t * 365 * 24 * 60 * 60 * 1000;
                break;
            case "M":
                t = t * 30 * 24 * 60 * 60 * 1000;
                break;
            case "w":
                t = t * 7 * 24 * 60 * 60 * 1000;
                break;
            case "d":
                t = t * 24 * 60 * 60 * 1000;
                break;
            case "h":
                t = t * 60 * 60 * 1000;
                break;
            case "m":
                t = t * 60 * 1000;
                break;
            default:
                break;
        }
        return t;
    }
    
    private static class SecurityHelperHolder {

        private static final SecurityHelper INSTANCE = new SecurityHelper();
    }
}
