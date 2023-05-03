package com.tspdevelopment.bluebeetle.csv.importmodels;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tobiesp
 */
@Data
@NoArgsConstructor
public class StudentV1 {
    private String student_name;
    private String group;
    private int grade;
    private String graduated;
    
    
    @Override
    public String toString() {
        return "StudentV1: {name: " + String.valueOf(student_name)
                + ",group: " + String.valueOf(group) 
                + ",grade: " + String.valueOf(grade) 
                + ",graduated: " + String.valueOf(graduated) + "}";
    }
}
