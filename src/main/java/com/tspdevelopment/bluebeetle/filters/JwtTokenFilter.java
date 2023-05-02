package com.tspdevelopment.bluebeetle.filters;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.data.repository.RoleRepository;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import com.tspdevelopment.bluebeetle.helpers.JwtTokenUtil;
import com.tspdevelopment.bluebeetle.services.controllerservice.AuthService;
import org.springframework.security.authentication.AuthenticationManager;

/**
 *
 * @author tobiesp
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil,
                   UserRepository userRepo,
                   RoleRepository roleRepo) {
        this.authService = new AuthService(jwtTokenUtil,
                   userRepo,
                   roleRepo);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Get jwt token and validate it is a valid token
        User user = this.authService.validJWToken(request);
        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UserDetails userDetails = (UserDetails)user;
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

}
