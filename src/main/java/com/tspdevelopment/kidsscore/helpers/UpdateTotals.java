package com.tspdevelopment.kidsscore.helpers;

import com.tspdevelopment.kidsscore.data.model.PointsEarned;
import com.tspdevelopment.kidsscore.data.model.PointsSpent;
import com.tspdevelopment.kidsscore.data.model.RunningTotals;
import com.tspdevelopment.kidsscore.data.model.Student;
import com.tspdevelopment.kidsscore.data.repository.PointsEarnedRepository;
import com.tspdevelopment.kidsscore.data.repository.PointsSpentRepository;
import com.tspdevelopment.kidsscore.data.repository.RunningTotalsRepository;
import com.tspdevelopment.kidsscore.data.repository.StudentRepository;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author tobiesp
 */
public class UpdateTotals {

    private PointsEarnedRepository peRepository;
    private PointsSpentRepository psRepository;
    private RunningTotalsRepository rtRepository;
    private StudentRepository sRepository;
    
    private UpdateTotals() {
    }
    
    public static UpdateTotals getInstance() {
        return UpdateTotalsHolder.INSTANCE;
    }
    
    public void setPointsEarnedRepository(PointsEarnedRepository peRepository) {
        if(this.peRepository == null)
            this.peRepository = peRepository;
    }
    
    public void setPointsSpentRepository(PointsSpentRepository psRepository) {
        if(this.psRepository == null)
            this.psRepository = psRepository;
    }
    
    public void setRunningTotalsRepository(RunningTotalsRepository rtRepository) {
        if(this.rtRepository == null)
            this.rtRepository = rtRepository;
    }
    
    public void setStudentRepository(StudentRepository sRepository) {
        if(this.sRepository == null)
            this.sRepository = sRepository;
    }
    
    /**
     * Update totals for all the students.
     */
    public void updateTotals() {
        List<Student> students = sRepository.findAll();
        List<PointsEarned> earnings = peRepository.findAll();
        List<PointsSpent> spent = psRepository.findAll();
        for(Student s: students) {
            int totalEarned = 0;
            int totalSpent = 0;
            for(PointsEarned pe: earnings) {
                if(pe.getStudent() == s) {
                    totalEarned += pe.getTotal();
                }
            }
            for(PointsSpent ps: spent) {
                if(ps.getStudent() == s) {
                    totalSpent += ps.getPoints();
                }
            }
            Optional<RunningTotals> total = rtRepository.findByStudent(s);
            if(total.isPresent()) {
                RunningTotals t = total.get();
                t.setTotal(totalEarned-totalSpent);
                rtRepository.save(t);
            } else {
                RunningTotals t = new RunningTotals();
                t.setStudent(s);
                t.setTotal(totalEarned-totalSpent);
                rtRepository.save(t);
            }
        }
    }
    
    public void updateTotal(Student s) {
        List<PointsEarned> earnings = peRepository.searchStudentEventDate(s, startOfYear(), endOfYear());
        List<PointsSpent> spent = psRepository.searchStudentEventDate(s, startOfYear(), endOfYear());
        int totalEarned = 0;
        int totalSpent = 0;
        for(PointsEarned pe: earnings) {
            if(pe.getStudent() == s) {
                totalEarned += pe.getTotal();
            }
        }
        for(PointsSpent ps: spent) {
            if(ps.getStudent() == s) {
                totalSpent += ps.getPoints();
            }
        }
        Optional<RunningTotals> total = rtRepository.findByStudent(s);
        if(total.isPresent()) {
            RunningTotals t = total.get();
            t.setTotal(totalEarned-totalSpent);
            rtRepository.save(t);
        } else {
            RunningTotals t = new RunningTotals();
            t.setStudent(s);
            t.setTotal(totalEarned-totalSpent);
            rtRepository.save(t);
        }
    }
    
    private LocalDate startOfYear() {
        return LocalDate.now().with(firstDayOfYear());
    }
    
    private LocalDate endOfYear() {
        return LocalDate.now().with(lastDayOfYear());
    }
    
    private static class UpdateTotalsHolder {

        private static final UpdateTotals INSTANCE = new UpdateTotals();
    }
}
