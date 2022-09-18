package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tspdevelopment.kidsscore.data.model.Student;

public interface StudentProvider extends BaseProvider<Student> {

    public Student create(Student newItem);

    public void delete(UUID id);

    public List<Student> findAll();

    public Optional<Student> findById(UUID id);

    public Student update(Student replaceItem, UUID id);

    public List<Student> search(Student item);
    
}
