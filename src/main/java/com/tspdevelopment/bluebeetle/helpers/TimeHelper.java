/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package com.tspdevelopment.bluebeetle.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

/**
 *
 * @author tobiesp
 */
public class TimeHelper {
    
    private TimeHelper() {
    }
    
    public static TimeHelper getInstance() {
        return TimeHelperHolder.INSTANCE;
    }

    public LocalDate startOfYear() {
        return LocalDate.now().with(firstDayOfYear());
    }

    public LocalDate endOfYear() {
        return LocalDate.now().with(lastDayOfYear());
    }
    
    public LocalDate getNextEventDate() {
        LocalDateTime ldt = LocalDateTime.now();
        if((ldt.getHour() > 18) && (ldt.getDayOfWeek() == DayOfWeek.WEDNESDAY)) {
            ldt = ldt.plusDays(1);
        }
        while(ldt.getDayOfWeek() != DayOfWeek.WEDNESDAY) {
            ldt = ldt.plusDays(1);
        }
        return ldt.toLocalDate();
    }
    
    public LocalDate getPreviousEventDate() {
        LocalDateTime ldt = LocalDateTime.now();
        if((ldt.getHour() > 18) && (ldt.getDayOfWeek() == DayOfWeek.WEDNESDAY)) {
            return ldt.toLocalDate();
        }
        ldt = ldt.minusDays(1);
        while(ldt.getDayOfWeek() != DayOfWeek.WEDNESDAY) {
            ldt = ldt.minusDays(1);
        }
        return ldt.toLocalDate();
    }
    
    private static class TimeHelperHolder {

        private static final TimeHelper INSTANCE = new TimeHelper();
    }
}
