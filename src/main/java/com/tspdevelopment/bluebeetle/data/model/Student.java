package com.tspdevelopment.bluebeetle.data.model;

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
public class Student implements BaseItem {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    @Type(type="uuid-char")
    private UUID id;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column
    private LocalDateTime modifiedAt;
    
    @Column(nullable=false, unique=true, length=1024)
    private String name;
    
    @ManyToOne
    private Group group;
    
    @Column
    private int grade;
    
    @Column
    private LocalDateTime graduated;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.toString().equals(other.id.toString()))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (modifiedAt == null) {
            if (other.modifiedAt != null)
                return false;
        } else if (!modifiedAt.equals(other.modifiedAt))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (grade != other.grade)
            return false;
        if (graduated == null) {
            if (other.graduated != null)
                return false;
        } else if (!graduated.equals(other.graduated))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((modifiedAt == null) ? 0 : modifiedAt.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + grade;
        result = prime * result + ((graduated == null) ? 0 : graduated.hashCode());
        return result;
    }
    
    
}
