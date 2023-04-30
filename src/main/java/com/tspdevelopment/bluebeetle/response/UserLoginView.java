package com.tspdevelopment.bluebeetle.response;

import lombok.Data;

/**
 *
 * @author tobiesp
 */
@Data
public class UserLoginView {

    private String id;

    private String username;
    private String firstName;
    private String lastName;
    private String token;

}
