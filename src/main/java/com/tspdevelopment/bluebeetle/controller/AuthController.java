package com.tspdevelopment.bluebeetle.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.data.repository.RoleRepository;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import com.tspdevelopment.bluebeetle.helpers.JwtToken;
import com.tspdevelopment.bluebeetle.helpers.JwtTokenUtil;
import com.tspdevelopment.bluebeetle.helpers.SecurityHelper;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.RoleProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.UserProviderImpl;
import com.tspdevelopment.bluebeetle.views.AuthRequest;
import com.tspdevelopment.bluebeetle.response.UserView;

/**
 *
 * @author tobiesp
 */
@RestController 
@RequestMapping(path = "/api/public")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserProviderImpl userProvider;
    private final RoleProviderImpl roleProvider;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager,
                   JwtTokenUtil jwtTokenUtil,
                   UserRepository userRepo,
                   RoleRepository roleRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userProvider = new UserProviderImpl(userRepo);
        this.roleProvider = new RoleProviderImpl(roleRepo);
    }
    
    @PostMapping("/login")
    public ResponseEntity<UserView> login(@RequestBody @Validated AuthRequest request){
        try {
            logger.info("Auth Request: " + request.toString());
            Authentication authenticate = authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                    )
                );

            SecurityContextHolder.getContext().setAuthentication(authenticate);
            User user = (User) authenticate.getPrincipal();
            if(!SecurityHelper.getInstance().validUser(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            JwtToken token = jwtTokenUtil.generateAccessToken(user);
            user.setTokenId(token.getId());
            userProvider.updateJwtTokenId(user.getId(), token.getId());
            return ResponseEntity.ok()
                .header(
                    HttpHeaders.AUTHORIZATION,
                    token.getToken()
                )
                .body(toUserView(user));
        } catch (BadCredentialsException ex) {
            this.userProvider.increaseLoginAttempt(request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader HttpHeaders headers){
        List<String> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed to access method.");
        }
        try {
            UUID userId = this.jwtTokenUtil.getUserId(authHeader.get(0).split(" ")[1]);
            this.userProvider.updateJwtTokenId(userId, null);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed to access method.");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/signup")
    User newItem(@RequestBody User newItem){
        newItem.clearAllRoles();
        Optional<Role> role = this.roleProvider.findByAuthority(Role.NO_ROLE);
        newItem.setAuthorities(role.get());
        return userProvider.create(newItem);
    }
    
    private UserView toUserView(User user) {
        UUID id = user.getId();
        if (id == null) {
            return null;
        }
        Optional<User> u = userProvider.findById(id);
        if(u.isPresent()) {
            UserView view = new UserView();
            view.setFullName(u.get().getFirstName() + " " + u.get().getLastName());
            view.setUsername(u.get().getUsername());
            view.setId(u.get().getId().toString());
            return view;
        } else {
            return null;
        }
    }
}
