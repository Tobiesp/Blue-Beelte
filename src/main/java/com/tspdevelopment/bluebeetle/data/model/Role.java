package com.tspdevelopment.bluebeetle.data.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author tobiesp
 */
@Entity(name="ROLE_TABLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements GrantedAuthority, BaseItem {
    
    public static final String NO_ROLE = "ROLE_NO_ROLE";
    public static final String ADMIN_ROLE = "ROLE_ADMIN_ROLE";
    public static final String READ_ROLE = "ROLE_READ_ROLE";
    public static final String WRITE_ROLE = "ROLE_WRITE_ROLE";
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type="uuid-char")
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(unique=true, nullable=false)
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

        if(r.authority == null) {
            if(this.authority == null) {
                return true;
            } else {
                return false;
            }
        }
        if(!r.authority.equals(this.authority)) {
            return false;
        }
        if (!r.id.toString().equals(this.id.toString())) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.authority);
        return hash;
    }
    
}
