package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.model.User;
import com.tspdevelopment.bluebeetle.data.repository.RoleRepository;
import com.tspdevelopment.bluebeetle.data.repository.UserRepository;
import com.tspdevelopment.bluebeetle.helpers.JwtToken;
import com.tspdevelopment.bluebeetle.helpers.JwtTokenUtil;
import com.tspdevelopment.bluebeetle.helpers.SecurityHelper;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.RoleProviderImpl;
import com.tspdevelopment.bluebeetle.provider.sqlprovider.UserProviderImpl;
import com.tspdevelopment.bluebeetle.response.UserLoginView;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author tobiesp
 */
@Service
public class AuthService {
    
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final UserProviderImpl userProvider;
    private final RoleProviderImpl roleProvider;

    public AuthService(JwtTokenUtil jwtTokenUtil,
                   UserRepository userRepo,
                   RoleRepository roleRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userProvider = new UserProviderImpl(userRepo);
        this.roleProvider = new RoleProviderImpl(roleRepo);
    }
    
    public UserLoginView login(User user) {
        if(!SecurityHelper.getInstance().validUser(user)) {
            return null;
        }
        JwtToken token = jwtTokenUtil.generateAccessToken(user);
        user.setTokenId(token.getId());
        userProvider.updateJwtTokenId(user.getId(), token.getId());
        return toUserView(user, token.getToken());
    }
    
    public void loginFailed(String username) {
        this.userProvider.increaseLoginAttempt(username);
    }
    
    public boolean logout(HttpHeaders headers){
        try {
            UUID userId = getUserIdFromToken(headers);
            this.userProvider.updateJwtTokenId(userId, null);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public User newUser(User newItem){
        Optional<Role> role = this.roleProvider.findByAuthority(Role.NO_ROLE);
        newItem.setAuthorities(role.get());
        return userProvider.create(newItem);
    }
    
    public UserDetails getUserFromToken(String token) {
        UserDetails userDetails = userProvider
                    .findById(jwtTokenUtil.getUserId(token))
                    .orElse(null);
        if(userDetails == null) {
            return null;
        }
        User user = (User)userDetails;
        if(user.getTokenId() == null) {
            return null;
        }
        if(!SecurityHelper.getInstance().validUser(user)) {
            return null;
        }
        return userDetails;
    }
    
    public User validJWToken(HttpHeaders headers) {
        List<String> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
        if((authHeader == null) || authHeader.isEmpty()) {
            return null;
        }
        String header = authHeader.get(0);
        return this.ValidToken(header);
    }
    
    public User validJWToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return this.ValidToken(header);
    }
    
    private User ValidToken(String header) {
        if (!hasText(header) || !header.startsWith("Bearer ")) {
            return null;
        }
        String token = getTokenString(header);
        if(token == null) {
            return null;
        }
        if(!this.jwtTokenUtil.validate(token)) {
            return null;
        }
        UserDetails userDetails = this.getUserFromToken(token);
        if(userDetails == null) {
            return null;
        }
        User user = (User)userDetails;
        if(user.getTokenId() == null) {
            return null;
        }
        if(!SecurityHelper.getInstance().validUser(user)) {
            return null;
        }
        UUID tokenId = jwtTokenUtil.getTokenId(token);
        if(user.getTokenId().toString().equals(tokenId.toString())) {
            return user;
        }
        return null;
    }
    
    private UserLoginView toUserView(User user, String token) {
        UUID id = user.getId();
        if (id == null) {
            return null;
        }
        Optional<User> u = userProvider.findById(id);
        if(u.isPresent()) {
            UserLoginView view = new UserLoginView();
            view.setFirstName(u.get().getFirstName());
            view.setLastName(u.get().getLastName());
            view.setUsername(u.get().getUsername());
            view.setId(u.get().getId().toString());
            Iterator<GrantedAuthority> it = u.get().getAuthorities().iterator();
            if(it.hasNext()){
                view.setUserRole((Role) it.next());
            }
            view.setToken(token);
            return view;
        } else {
            return null;
        }
    }
    
    private String getTokenString(String header) {
        String[] s = header.split(" ");
        if(s.length == 1) {
            return null;
        }
        return s[1];
    }
    
    private UUID getUserIdFromToken(HttpHeaders headers) {
        List<String> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) {
            return null;
        }
        if(authHeader.isEmpty()) {
            return null;
        }
        String[] s = authHeader.get(0).split(" ");
        if(s.length < 2) {
            return null;
        }
        return this.jwtTokenUtil.getUserId(s[1]);
    }

    
}
