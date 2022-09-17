/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.KidsScore.configuration;

import com.tspdevelopment.KidsScore.properties.JwtProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tobiesp
 */
@Configuration
public class PropertiesCongifuration {
    
    @Bean
    @ConfigurationProperties(prefix = "simple.rmaj.jwt")
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }
    
}
