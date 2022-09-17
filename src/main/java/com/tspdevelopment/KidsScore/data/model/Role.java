package com.tspdevelopment.KidsScore.data.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author tobiesp
 */
@Entity(name="ROLE_TABLE")
@Data @AllArgsConstructor @NoArgsConstructor
public class Role implements GrantedAuthority {
    
    public static final String ADMIN_ROLE = "ADMIN_ROLE";
    public static final String READ_ROLE = "READ_ROLE";
    public static final String WRITE_ROLE = "WRITE_ROLE";
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(unique=true,nullable=false)
    private String authority;
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
    
    public Role(String authority) {
        this.authority = authority;
    }
    
    @Override
    public String toString() {
        return this.authority;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof Role)) {
            return false;
        }
        Role r = (Role) o;
        
        return (r.authority == null ? this.authority == null : r.authority.equals(this.authority)) && r.id == this.id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.authority);
        return hash;
    }
    
}
