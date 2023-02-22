package com.tspdevelopment.kidsscore.data.model;

import java.time.LocalDateTime;
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

/**
 *
 * @author tobiesp
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunningTotals implements BaseItem {
    
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
    @Column
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column
    private LocalDateTime modifiedAt;
    
    @ManyToOne
    private Student student;
    
    @Column
    private int total;
    
}
