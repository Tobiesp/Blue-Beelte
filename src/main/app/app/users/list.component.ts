import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { AccountService } from '@app/_services';
import { User } from '@app/_models';

@Component({ templateUrl: 'list.component.html' })
export class ListComponent implements OnInit {
    users?: any[];

    constructor(private accountService: AccountService) {}

    ngOnInit() {
        this.accountService.getAllUsers()
            .pipe(first())
            .subscribe(users => this.users = users);
    }

    deleteUser(id: string | undefined) {
        if(id === undefined) {
            return;
        }
        const user = this.users!.find(x => x.id === id);
        user!.isDeleting = true;
        this.accountService.deleteUser(id)
            .pipe(first())
            .subscribe(() => this.users = this.users);
    }
}