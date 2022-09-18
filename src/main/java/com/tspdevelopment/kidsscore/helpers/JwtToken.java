/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.kidsscore.helpers;

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
