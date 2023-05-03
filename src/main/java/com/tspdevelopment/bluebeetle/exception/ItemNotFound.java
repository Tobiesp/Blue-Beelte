/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.bluebeetle.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author tobiesp
 */
public class ItemNotFound extends ResponseStatusException {

    /**
     * Creates a new instance of <code>ItemNotFound</code> without detail
     * message.
     * @param id - id of the item we are looking for
     */
    public ItemNotFound(UUID id) {
        super(HttpStatus.NOT_FOUND, "Unable to find item with id: " + id.toString());
    }
}
