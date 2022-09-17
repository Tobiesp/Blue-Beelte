/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.KidsScore.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tspdevelopment.KidsScore.helpers.SecurityHelper;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author tobiesp
 */
@Entity(name="USER_TABLE")
@Data
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    
    @Column()
    private boolean enabled = true;

    @Column(unique=true,nullable=false)
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private UUID tokenId;
    private String fullName;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private List<GrantedAuthority> roles = new ArrayList<>();
    
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new HashSet<>(this.roles);
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.enabled;
    }
    
    public void setPassword(String password) {
        if(!SecurityHelper.getInstance().validPassword(password)) {
            throw new IllegalArgumentException("Password doesn't meet all the criteria.");
        }
        this.password = SecurityHelper.getInstance().encodePassword(password);
    }
    
}
