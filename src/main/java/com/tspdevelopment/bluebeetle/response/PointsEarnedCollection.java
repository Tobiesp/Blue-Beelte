package com.tspdevelopment.bluebeetle.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tspdevelopment.bluebeetle.data.model.PointCategory;
import com.tspdevelopment.bluebeetle.data.model.PointsEarned;
import com.tspdevelopment.bluebeetle.data.model.Student;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Data;

/**
 *
 * @author tobiesp
 */
@Data
public class PointsEarnedCollection {
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/d/yyyy")
    private LocalDate eventDate;
    private Student student;
    private HashMap<PointCategory, PointsEarned> points;
    
    public void addPoint(PointsEarned pe) {
        if(points == null) {
            this.points = new HashMap<>();
        }
        this.points.put(pe.getPointCategory(), pe);
    }
    
    public void removePoint(PointsEarned pe) {
        if(points == null) {
            return;
        }
        this.points.remove(pe.getPointCategory());
    }
    
    public void setPoints(List<PointsEarned> list) {
        if(points == null) {
            this.points = new HashMap<>();
        }
        for(PointsEarned pe : list) {
            this.points.put(pe.getPointCategory(), pe);
        }
    }
    
    public List<PointsEarned> getPoints() {
        if(points == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.points.values());
    }
    
}
