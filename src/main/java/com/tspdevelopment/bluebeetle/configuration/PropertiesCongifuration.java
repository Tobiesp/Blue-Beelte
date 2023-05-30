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
