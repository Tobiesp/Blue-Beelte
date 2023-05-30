package com.tspdevelopment.bluebeetle.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tspdevelopment.bluebeetle.data.model.JWTSecret;
import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.properties.JwtProperties;
import com.tspdevelopment.bluebeetle.data.repository.*;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.*;
import java.util.Base64;

/**
 *
 * @author tobiesp
 */
@Component
public class JwtTokenUtil {

    private String jwtSecret;
    private String secretId;
    private LocalDateTime secretTime;
    private final String jwtIssuer;
    private final long experationTime;
    private final JwtProperties jwtProperties;
    private final JWTSecretProviderImpl provider;

    private final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    
    public JwtTokenUtil(@Autowired JwtProperties jwtProperties, @Autowired JWTSecretRepository jwtr) {
        if(jwtProperties == null) {
            logger.info("jwtProperties is null!");
        }
        this.provider = new JWTSecretProviderImpl(jwtr);
        this.jwtProperties = jwtProperties;
        jwtSecret = this.jwtProperties.getSecret();
        jwtIssuer = this.jwtProperties.getIssuer();
        experationTime = SecurityHelper.getInstance().processExperationTime(this.jwtProperties.getExperation());
    }

    private void generateNewJWTSecert() {
        if((secretTime == null) || secretTime.isBefore(LocalDateTime.now())) {
            this.jwtSecret = SecurityHelper.getInstance().generateSecret();
            this.secretId = SecurityHelper.getInstance().generateSecretId();
            this.secretTime = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
            JWTSecret jwt = new JWTSecret();
            jwt.setCreatedAt(LocalDateTime.now());
            jwt.setSecret(jwtSecret);
            jwt.setSecretId(secretId);
            provider.create(jwt);
            provider.cleanUpSecret(LocalDateTime.now().minusSeconds((this.experationTime/1000)));
        }
    }

    private String getJWTSecret(String secretId) {
        if(secretId.equals(this.secretId)) {
            return this.jwtSecret;
        } else {
            Optional<JWTSecret> secret = provider.findBySecretId(secretId);
            if(secret.isPresent()) {
                return secret.get().getSecret();
            } else {
                throw new NullPointerException("Secret not found");
            }
        }
    }

    public JwtToken generateAccessToken(User user) {
        generateNewJWTSecert();
        UUID uuid = UUID.randomUUID();
        JwtToken token = new JwtToken(Jwts.builder()
                .setId(uuid.toString())
                .setSubject(user.getId().toString())
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + experationTime))
                .claim("sid", this.secretId)
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
    
    private String getSID(String token) {
        if((token == null) || token.trim().isEmpty()) {
            return null;
        }
        String body = token.split("\\.")[1];
        String claims = new String(Base64.getDecoder().decode(body));
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode actualObj = mapper.readTree(claims);
            return actualObj.get("sid").asText();
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    public boolean validate(String token) {
        if(token == null) {
            logger.error("Token can not be null.");
            return false;
        }
        try {
            logger.info("Valid JWT token: " + token);
            String sid = getSID(token);
            if(sid != null){
                logger.info("Found sid: " + sid);
                String secret = this.getJWTSecret(sid);
                if((secret == null) || secret.trim().isEmpty()) {
                    logger.info("JWT secret key not found!");
                    return false;
                }
                logger.info("JWT secret key found: " + secret);
                Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
                return true;
            } else {
                logger.error("Invalid JWT token - No sid.");
                return false;
            }
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
