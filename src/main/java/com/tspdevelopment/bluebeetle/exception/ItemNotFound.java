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
