/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.bluebeetle.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tspdevelopment.bluebeetle.properties.JwtProperties;

/**
 *
 * @author tobiesp
 */
@Configuration
public class PropertiesCongifuration {
    
    @Bean
    @ConfigurationProperties(prefix = "blue.beetle.jwt")
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }
    
}
