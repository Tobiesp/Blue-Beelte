package com.tspdevelopment.bluebeetle.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.data.repository.RoleRepository;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import com.tspdevelopment.bluebeetle.helpers.JwtTokenUtil;
import com.tspdevelopment.bluebeetle.views.AuthRequest;
import com.tspdevelopment.bluebeetle.response.UserLoginView;
import com.tspdevelopment.bluebeetle.services.controllerservice.AuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author tobiesp
 */
@RestController 
@RequestMapping(path = "/api/public")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager, 
                JwtTokenUtil jwtTokenUtil,
                UserRepository userRepo,
                RoleRepository roleRepo) {
        this.authService = new AuthService(jwtTokenUtil, userRepo, roleRepo);
        this.authenticationManager = authenticationManager;
    }
    
    @PostMapping("/login")
    public ResponseEntity<UserLoginView> login(@RequestBody @Validated AuthRequest request){
        try {
            Authentication authenticate = authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                    )
                );

            SecurityContextHolder.getContext().setAuthentication(authenticate);
            User user = (User) authenticate.getPrincipal();
            
            UserLoginView view = authService.login(user);
            if(view == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else {
                return ResponseEntity.ok()
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        view.getToken()
                    )
                    .body(view);
            }
        } catch (BadCredentialsException ex) {
            this.authService.loginFailed(request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        
        
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader HttpHeaders headers){
        if(this.authService.logout(headers)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed to access method.");
        }
    }

    @PostMapping("/signup")
    public User newUser(@RequestBody User newItem){
        return this.authService.newUser(newItem);
    }
    
    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestHeader HttpHeaders headers) {
        User user = this.authService.validJWToken(headers);
        if(user != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }
}
