package com.tspdevelopment.bluebeetle.helpers;

import com.tspdevelopment.bluebeetle.data.model.RunningTotals;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.repository.PointsEarnedRepository;
import com.tspdevelopment.bluebeetle.data.repository.PointsSpentRepository;
import com.tspdevelopment.bluebeetle.data.repository.RunningTotalsRepository;
import com.tspdevelopment.bluebeetle.data.repository.StudentRepository;
import java.time.LocalDateTime;
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
        if (this.peRepository == null) {
            this.peRepository = peRepository;
        }
    }

    public void setPointsSpentRepository(PointsSpentRepository psRepository) {
        if (this.psRepository == null) {
            this.psRepository = psRepository;
        }
    }

    public void setRunningTotalsRepository(RunningTotalsRepository rtRepository) {
        if (this.rtRepository == null) {
            this.rtRepository = rtRepository;
        }
    }

    public void setStudentRepository(StudentRepository sRepository) {
        if (this.sRepository == null) {
            this.sRepository = sRepository;
        }
    }

    /**
     * Update totals for all the students.
     */
    public void updateTotals() {
        List<Student> students = sRepository.findAll();

        for (Student s : students) {
            Long totalEarned = peRepository.getPointSum(s);
            if (totalEarned == null) {
                totalEarned = 0l;
            }
            Long totalSpent = psRepository.getPointSum(s);
            if (totalSpent == null) {
                totalSpent = 0l;
            }

            Optional<RunningTotals> total = rtRepository.findByStudent(s);
            if (total.isPresent()) {
                RunningTotals t = total.get();
                t.setTotal((int) (totalEarned - totalSpent));
                t.setModifiedAt(LocalDateTime.now());
                rtRepository.save(t);
            } else {
                RunningTotals t = new RunningTotals();
                t.setStudent(s);
                t.setTotal((int) (totalEarned - totalSpent));
                t.setCreatedAt(LocalDateTime.now());
                rtRepository.save(t);
            }
        }
    }

    public void updateTotal(Student s) {
        Long totalEarned = peRepository.getPointSum(s);
        if (totalEarned == null) {
            totalEarned = 0l;
        }
        Long totalSpent = psRepository.getPointSum(s);
        if (totalSpent == null) {
            totalSpent = 0l;
        }
        Optional<RunningTotals> total = rtRepository.findByStudent(s);
        if (total.isPresent()) {
            RunningTotals t = total.get();
            t.setTotal((int) (totalEarned - totalSpent));
            rtRepository.save(t);
        } else {
            RunningTotals t = new RunningTotals();
            t.setStudent(s);
            t.setTotal((int) (totalEarned - totalSpent));
            rtRepository.save(t);
        }
    }

    // private LocalDate startOfYear() {
    //     return LocalDate.now().with(firstDayOfYear());
    // }

    // private LocalDate endOfYear() {
    //     return LocalDate.now().with(lastDayOfYear());
    // }

    private static class UpdateTotalsHolder {

        private static final UpdateTotals INSTANCE = new UpdateTotals();
    }
}
