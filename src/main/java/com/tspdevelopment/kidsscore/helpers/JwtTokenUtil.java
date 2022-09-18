/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.kidsscore.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tspdevelopment.kidsscore.data.model.User;
import com.tspdevelopment.kidsscore.properties.JwtProperties;

/**
 *
 * @author tobiesp
 */
@Component
public class JwtTokenUtil {

    private final String jwtSecret;
    private final String jwtIssuer;
    private final long experationTime;
    private final JwtProperties jwtProperties;

    private final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);;
    
    public JwtTokenUtil(@Autowired JwtProperties jwtProperties) {
        if(jwtProperties == null) {
            logger.info("jwtProperties is null!");
        }
        this.jwtProperties = jwtProperties;
        jwtSecret = this.jwtProperties.getSecret();
        jwtIssuer = this.jwtProperties.getIssuer();
        experationTime = SecurityHelper.getInstance().processExperationTime(this.jwtProperties.getExperation());
    }

    public JwtToken generateAccessToken(User user) {
        UUID uuid = UUID.randomUUID();
        JwtToken token = new JwtToken(Jwts.builder()
                .setId(uuid.toString())
                .setSubject(user.getId().toString())
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + experationTime)) // 1 week
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact(), uuid);
        return token;
    }

    public UUID getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        logger.info("Token Subject: " + claims.getSubject());
        return UUID.fromString(claims.getSubject());
    }
    
    public UUID getTokenId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return UUID.fromString(claims.getId());
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }
}
