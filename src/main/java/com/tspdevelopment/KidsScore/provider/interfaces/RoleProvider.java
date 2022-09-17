package com.tspdevelopment.KidsScore.provider.interfaces;

import com.tspdevelopment.KidsScore.data.model.Role;

/**
 *
 * @author tobiesp
 */
public interface RoleProvider extends BaseProvider<Role>{
    
    public Role create(Role item);
}
