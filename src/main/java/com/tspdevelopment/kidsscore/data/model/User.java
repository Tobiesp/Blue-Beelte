package com.tspdevelopment.kidsscore.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tspdevelopment.kidsscore.helpers.SecurityHelper;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Column()
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column()
    private LocalDateTime modifiedAt;
    
    @Column()
    @Builder.Default
    private boolean enabled = true;

    @Column(unique=true,nullable=false)
    private String username;
    @JsonIgnore
    @Column()
    private String password;
    @JsonIgnore
    @Column()
    private UUID tokenId;
    @Column()
    private String fullName;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @Builder.Default
    private List<Role> roles = new ArrayList<>();
    
    public void setAuthorities(GrantedAuthority role) {
        roles.add((Role)role);
    }
    
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User: {\n");
        sb.append("username: ").append(this.username).append(",\n");
        sb.append("fullname: ").append(this.fullName).append(",\n");
        sb.append("password: ").append(this.password).append(",\n");
        if(!this.roles.isEmpty()) {
            for(Role r : roles) {
                sb.append("role: ").append(r.getAuthority()).append(",\n");
            }
        }
        return sb.toString();
    }
    
}
