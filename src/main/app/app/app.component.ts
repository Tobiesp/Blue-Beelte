import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AccountService } from './_services';
import { User } from './_models';

@Component({ selector: 'app-root', templateUrl: 'app.component.html' })
export class AppComponent {
    user?: User | null;

    constructor(
        private router: Router,
        private accountService: AccountService) {
        this.accountService.user.subscribe(x => this.user = x);
    }

    logout() {
        this.accountService.logout();
    }

    NavigateToHome() {
        this.router.navigate(['/']);
    }

    NavigateToStudents() {
        this.router.navigate(['/students']);
    }

    NavigateToUsers() {
        this.router.navigate(['/users']);
    }

    NavigateToAddEarnedPoints() {
        this.router.navigate(['/points/earned']);
    }

    NavigateToAddSpentPoints() {
        this.router.navigate(['/points/spent']);
    }

    NavigateToPointsHistory() {
        this.router.navigate(['/points/history']);
    }

    NavigateToEarnedPointsCheckIn() {
        this.router.navigate(['/checkin/earned']);
    }

    NavigateToSpentPointsCheckIn() {
        this.router.navigate(['/checkin/spent']);
    }
}