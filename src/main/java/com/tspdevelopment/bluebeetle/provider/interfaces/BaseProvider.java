/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.bluebeetle.provider.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author tobiesp
 * @param <T> The entity type that we will extend.
 */
public interface BaseProvider<T> {
    public List<T> findAll();
    
    public Optional<T> findById(UUID id);
    
    public T update(T replaceItem, UUID id);
    
    public T create(T newItem);
    
    public void delete(UUID id);
    
    public List<T> search(T item);
}
