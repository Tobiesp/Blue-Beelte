import { Component, OnInit } from '@angular/core';

import { LastEventView, User, RunningTotals } from '@app/_models';
import { AccountService, ReportService, RunningTotalsService } from '@app/_services';

@Component({ templateUrl: 'home.component.html' })
export class HomeComponent implements OnInit {
    user: User | null;
    events?: LastEventView;
    totals?: RunningTotals[];

    constructor(private accountService: AccountService, private reportService: ReportService, private runningTotals: RunningTotalsService) {
        this.user = this.accountService.userValue;
    }
    
    ngOnInit() {
        this.reportService.getLastEvent()
            .subscribe(events => this.events = events);
        this.runningTotals.getAllTotals()
            .subscribe(totals => this.totals = totals);
    }
}