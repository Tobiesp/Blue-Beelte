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

    hasNoAccess(): boolean {
        return this.accountService.hasNoAccess();
    }

    hasReadAccess(): boolean {
        return this.accountService.hasReadAccess();
    }

    hasWriteAccess(): boolean {
        return this.accountService.hasWriteAccess();
    }

    hasAdminAccess(): boolean {
        return this.accountService.hasAdminAccess();
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

    NavigateToAddEarnedPoints() {
        this.router.navigate(['/points/earned/add']);
    }

    NavigateToAddSpentPoints() {
        this.router.navigate(['/points/spent/add']);
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

    NavigateToUsers() {
        this.router.navigate(['/users']);
    }

    NavigateToConfig() {
        this.router.navigate(['/config']);
    }

    NavigateToImport() {
        this.router.navigate(['/import']);
    }

    NavigateToExport() {
        this.router.navigate(['/export']);
    }

    navigateToUserConfig() {
        this.router.navigate(['/user/config']);
    }
}