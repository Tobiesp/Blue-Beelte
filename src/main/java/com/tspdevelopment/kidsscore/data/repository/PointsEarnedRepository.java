/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.kidsscore.data.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tspdevelopment.kidsscore.data.model.PointsEarned;
import com.tspdevelopment.kidsscore.data.model.Student;

/**
 *
 * @author tobiesp
 */
public interface PointsEarnedRepository extends JpaRepository<PointsEarned, UUID> {
    public Optional<PointsEarned> findByStudent(Student student);
    public List<PointsEarned> findByAttended(Boolean attended);
    public List<PointsEarned> findByBible(Boolean bible);
    public List<PointsEarned> findByBibleVerse(Boolean bibleVerse);
    public List<PointsEarned> findByBringAFriend(Boolean bringAFriend);
    public List<PointsEarned> findByAttentive(Boolean attentive);
    public List<PointsEarned> findByRecallsLastWeekLesson(Boolean recallsLastWeekLesson);
    public List<PointsEarned> findByEventDate(Date eventDate);
    @Query("SELECT u FROM PointsEarned u WHERE u.eventDate >= ?1 and u.eventDate <= ?2")
    public List<PointsEarned> searchEventDate(Date start, Date end);
    
}
