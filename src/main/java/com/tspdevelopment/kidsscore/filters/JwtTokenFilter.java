package com.tspdevelopment.kidsscore.filters;

import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tspdevelopment.kidsscore.data.model.User;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import com.tspdevelopment.kidsscore.helpers.JwtTokenUtil;
import com.tspdevelopment.kidsscore.helpers.SecurityHelper;
import javax.servlet.http.Cookie;

/**
 *
 * @author tobiesp
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepo;
    private final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil,
            UserRepository userRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Get jwt token and validate
        final String token = getToken(request);
        if (!jwtTokenUtil.validate(token)) {
            logger.info("Invalid JWT token!");
            filterChain.doFilter(request, response);
            return;
        }

        try {
// Get user identity and set it on the spring security context
            UserDetails userDetails = userRepo
                    .findById(jwtTokenUtil.getUserId(token))
                    .orElse(null);
            if(userDetails == null) {
                logger.info("Null user for: " + jwtTokenUtil.getUserId(token));
                filterChain.doFilter(request, response);
                return;
            }
            User user = (User)userDetails;
            if(user.getTokenId() == null) {
                logger.info("user has no token Id.");
                filterChain.doFilter(request, response);
                return;
            }
            if(!SecurityHelper.getInstance().validUser(user)) {
                filterChain.doFilter(request, response);
                return;
            }
            UUID tokenId = jwtTokenUtil.getTokenId(token);
            if(!user.getTokenId().toString().equals(tokenId.toString())){
                logger.info("user token Id does not match jwt id: " + user.getTokenId().toString() + " != " + tokenId.toString());
                filterChain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities()
            );
            
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException iOException) {
            throw iOException;
        } catch (IllegalArgumentException illegalArgumentException) {
            filterChain.doFilter(request, response);
        }
    }
    
    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (hasText(header) && header.startsWith("Bearer ")) {
            return header.split(" ")[1].trim();
        }
        Cookie[] cookies = request.getCookies();
        if((cookies == null) || (cookies.length == 0)) {
            return null;
        }
        for(Cookie c : cookies) {
            if(c.getName().equals(SecurityHelper.getInstance().getCookieName()) && c.isHttpOnly() && c.getSecure()) {
                return c.getValue();
            }
        }
        return null;
    }

}
