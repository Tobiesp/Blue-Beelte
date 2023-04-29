package com.tspdevelopment.bluebeetle.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author tobiesp
 */
@Data
public class LastEventView {
    private LocalDate eventDate;
    private List<GroupCountView> groups;
    private int total;

    public LastEventView() {
        this.groups = new ArrayList<>();
    }
    
    public void addGroup(GroupCountView gcv) {
        this.groups.add(gcv);
    }
    
    
    
}
