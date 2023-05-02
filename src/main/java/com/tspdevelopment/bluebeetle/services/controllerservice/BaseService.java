/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.bluebeetle.services.controllerservice;

import com.tspdevelopment.bluebeetle.data.model.BaseItem;
import com.tspdevelopment.bluebeetle.provider.interfaces.BaseProvider;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author tobiesp
 * @param <T>
 * @param <R>
 */
@Service
public abstract class BaseService<T extends BaseItem, R extends BaseProvider<T>> {
    protected R provider;
    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(getGenericName());
    
    public List<T> getAllItems(){
        List<T> cList = provider.findAll();
        return cList;
    }
    
    public T getNewItem(T newItem){
        return provider.create(newItem);
    }
    
    public T getItem(UUID id){
        Optional<T> c = provider.findById(id);
        if(c.isPresent()){
            return c.get();
        } else {
            return null;
        }
    }
    
    public T replaceItem(T replaceItem, UUID id){
        return provider.update(replaceItem, id);
    }
    
    public void deleteItem(UUID id){
        this.provider.delete(id);
    }
    
    public List<T> search(T item){
        List<T> cList = provider.search(item);
        return cList;
    }
    
    private String getGenericName(){
        return ((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]).getTypeName();
    }
    
}
