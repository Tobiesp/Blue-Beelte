package com.tspdevelopment.kidsscore.csv.importmodels;

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
}
