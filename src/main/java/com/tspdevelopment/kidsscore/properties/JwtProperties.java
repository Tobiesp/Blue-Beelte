/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.kidsscore.properties;

import lombok.Data;

/**
 *
 * @author tobiesp
 */
@Data
public class JwtProperties {
    
    private String issuer = "example.io";
    private String secret = "zdtlD3JK56m6wTTgsNFhqzjqP";
    private String experation = "1w";
}
