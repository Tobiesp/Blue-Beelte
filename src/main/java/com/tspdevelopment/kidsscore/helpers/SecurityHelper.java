/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.kidsscore.helpers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author tobiesp
 */
public class SecurityHelper {
    
    private SecurityHelper() {
    }
    
    public static SecurityHelper getInstance() {
        return SecurityHelperHolder.INSTANCE;
    }
    
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
        if(capitalFlag && symbolFlag && numberFlag) {
            return true;
        }
        return false;
    }
    
    public long processExperationTime(String time) {
        long t = 1l;
        //Validate string
        //Valid chars y,M,w,d,h,m,s,ms
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
            case "s":
                t = t * 1000;
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
