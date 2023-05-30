package com.tspdevelopment.bluebeetle.helpers;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author tobiesp
 */
@Data @AllArgsConstructor
public class JwtToken {
    
    private String token;
    private UUID id;
    
    
    
}
