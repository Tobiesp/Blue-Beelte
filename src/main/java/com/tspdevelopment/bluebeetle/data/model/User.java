package com.tspdevelopment.bluebeetle.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tspdevelopment.bluebeetle.helpers.SecurityHelper;

import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails, BaseItem{
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type="uuid-char")
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

    @Column
    private String email;

    @JsonIgnore
    @Column()
    private String password;

    @JsonIgnore
    @Column(nullable = true)
    @Type(type="uuid-char")
    private UUID tokenId;

    @Column()
    private int failedAttempt;

    @Column()
    private LocalDateTime lockedTime;

    @Column()
    private String firstName;

    @Column()
    private String lastName;

    @ManyToOne()
    private Role userRole;
    
    @JsonIgnore
    public void setAuthorities(GrantedAuthority role) {
        this.userRole = (Role)role;
    }
    
    @JsonIgnore
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        HashSet<GrantedAuthority> hs = new HashSet<>();
        hs.add(userRole);
        return hs;
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
        sb.append("fullname: ").append(this.firstName).append("").append(this.lastName).append(",\n");
        sb.append("password: ").append(this.password.trim()).append(",\n");
        sb.append("role: ").append(this.userRole.getAuthority()).append(",\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (id == null) {
            if (other.id != null){
                return false;
            }
        } else if (!id.equals(other.id)){
            return false;
        }
        if (enabled != other.enabled){
            return false;
        }
        if (username == null) {
            if (other.username != null){
                return false;
            }
        } else if (!username.equals(other.username)){
            return false;
        }
        if (email == null) {
            if (other.email != null){
                return false;
            }
        } else if (!email.equals(other.email)){
            return false;
        }
        if (password == null) {
            if (other.password != null){
                return false;
            }
        } else if (!password.equals(other.password)){
            return false;
        }
        if (firstName == null) {
            if (other.firstName != null){
                return false;
            }
        } else if (!firstName.equals(other.firstName)){
            return false;
        }
        if (lastName == null) {
            if (other.lastName != null){
                return false;
            }
        } else if (!lastName.equals(other.lastName)){
            return false;
        }
        if (userRole == null) {
            return other.userRole == null;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((userRole == null) ? 0 : userRole.hashCode());
        return result;
    }
    
}
