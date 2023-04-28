package com.tspdevelopment.bluebeetle.csv.importmodels;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tobiesp
 */
@Data
@NoArgsConstructor
public class PointsSpentV1 {
    private String student;
    private String event_date;
    private int points_spent;
}
