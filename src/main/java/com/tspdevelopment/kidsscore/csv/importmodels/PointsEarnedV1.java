package com.tspdevelopment.kidsscore.csv.importmodels;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tobiesp
 */
@Data
@NoArgsConstructor
public class PointsEarnedV1 {
    private String event_date;
    private String student_name;
    private boolean attended;
    private boolean bible;
    private boolean bible_verse;
    private boolean bring_a_friend;
    private boolean attentive;
    private boolean recalls_last_week_lesson;
    private int total_points;
    
}
