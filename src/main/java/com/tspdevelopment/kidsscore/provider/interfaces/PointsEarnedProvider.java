package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tspdevelopment.kidsscore.data.model.PointsEarned;

public interface PointsEarnedProvider extends BaseProvider<PointsEarned>{

    public PointsEarned create(PointsEarned newItem);

    public void delete(UUID id);

    public List<PointsEarned> findAll();

    public Optional<PointsEarned> findById(UUID id);

    public PointsEarned update(PointsEarned replaceItem, UUID id);

    public List<PointsEarned> search(PointsEarned item);

    public List<PointsEarned> findByAttended(Boolean attended);

    public List<PointsEarned> findByBible(Boolean bible);

    public List<PointsEarned> findByBibleVerse(Boolean bibleVerse);

    public List<PointsEarned> findByBringAFriend(Boolean bringAFriend);

    public List<PointsEarned> findByAttentive(Boolean attentive);

    public List<PointsEarned> findByRecallsLastWeekLesson(Boolean recallsLastWeekLesson);
    
    public List<PointsEarned> findByEventDate(Date eventDate);
    
    public List<PointsEarned> searchEventDate(Date start, Date end);
    
}
