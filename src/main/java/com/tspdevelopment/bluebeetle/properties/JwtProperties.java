package com.tspdevelopment.bluebeetle.properties;

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
