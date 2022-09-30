package com.tspdevelopment.kidsscore.data.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author tobiesp
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JWTSecret implements BaseItem{
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(unique=true, nullable=false)
    private String secretId;
    @Column(unique=true, nullable=false)
    private String secret;
    
}
