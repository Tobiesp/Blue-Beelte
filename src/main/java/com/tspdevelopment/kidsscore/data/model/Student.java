package com.tspdevelopment.kidsscore.data.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 *
 * @author tobiesp
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student implements BaseItem {
    
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
    
    @Column(nullable=false, unique=true, length=1024)
    private String name;
    
    @ManyToOne
    private Group group;
    
    @Column
    private int grade;
    
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date graduated;
    
    
}
