package com.tspdevelopment.kidsscore.data.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tobiesp
 */
@Entity(name="GROUP_TABLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group implements BaseItem {
    
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
    
    @Column(unique=true, length=1024)
    private String name;
    
    @Override
    public String toString() {
        return "{Id: " + String.valueOf(id) + 
               ", Group: " + String.valueOf(name) + "}";
    }
}
