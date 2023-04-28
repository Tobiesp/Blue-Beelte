package com.tspdevelopment.bluebeetle.csv.importmodels;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tobiesp
 */
@Data
@NoArgsConstructor
public class PointTypeV1 {
    private String itemName;
    private String group;
    private int pointValue;
    
}
