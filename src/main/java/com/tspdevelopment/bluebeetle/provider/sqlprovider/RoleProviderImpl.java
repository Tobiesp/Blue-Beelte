package com.tspdevelopment.bluebeetle.provider.sqlprovider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.bluebeetle.data.model.Role;
import com.tspdevelopment.bluebeetle.data.repository.RoleRepository;
import com.tspdevelopment.bluebeetle.provider.interfaces.RoleProvider;

/**
 *
 * @author tobiesp
 */
public class RoleProviderImpl implements RoleProvider {
    
    private final RoleRepository repository;
    
    public RoleProviderImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Role> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return this.repository.findById(id);
    }

    public Optional<Role> findByAuthority(String authority) {
        return this.findByAuthority(authority);
    }

    @Override
    public Role update(Role replaceItem, UUID id) {
        throw new UnsupportedOperationException("Roles can not be updated.");
    }

    @Override
    public void delete(UUID id) {
        throw new UnsupportedOperationException("Roles can not be deleted.");
    }

    @Override
    public List<Role> search(Role item) {
        Example<Role> example = Example.of(item);
        return this.repository.findAll(example);
    }

    @Override
    public Role create(Role item) {
        return this.repository.save(item);
    }
    
}
