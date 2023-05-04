import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { User, Role } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class AccountService {
    private userSubject: BehaviorSubject<User | null>;
    public user: Observable<User | null>;

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        this.userSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('user')!));
        this.user = this.userSubject.asObservable();
    }

    public get userValue() {
        return this.userSubject.value;
    }

    login(username: string, password: string) {
        return this.http.post<User>(`${environment.apiUrl}/api/public/login`, { username, password })
            .pipe(map(user => {
                // store user details and jwt token in local storage to keep user logged in between page refreshes
                localStorage.setItem('user', JSON.stringify(user));
                this.userSubject.next(user);
                return user;
            }));
    }

    logout() {
        this.http.post<User>(`${environment.apiUrl}/api/public/logout`, {}).pipe();
        // remove user from local storage and set current user to null
        localStorage.removeItem('user');
        this.userSubject.next(null);
        this.router.navigate(['/account/login']);
    }

    register(user: User) {
        return this.http.post(`${environment.apiUrl}/api/users/signup`, user);
    }

    getAllUsers() {
        return this.http.get<User[]>(`${environment.apiUrl}/api/users/`);
    }

    getUserById(id: string) {
        return this.http.get<User>(`${environment.apiUrl}/api/users/${id}`);
    }

    updateUser(id: string, params: any) {
        if(id == this.userValue?.id) {
            if(params['role']) {
                delete params['role'];
            }
        }
        if(params['role']) {
            this.getRoleById(params['role']).pipe().subscribe(x => params['role'] = x);
        }

        return this.http.put(`${environment.apiUrl}/api/users/${id}`, params)
            .pipe(map(x => {
                // update stored user if the logged in user updated their own record
                if (id == this.userValue?.id) {
                    // update local storage
                    const user = { ...this.userValue, ...params };
                    localStorage.setItem('user', JSON.stringify(user));

                    // publish updated user to subscribers
                    this.userSubject.next(user);
                }
                return x;
            }));
    }

    deleteUser(id: string) {
        return this.http.delete(`${environment.apiUrl}/api/users/${id}`)
            .pipe(map(x => {
                // auto logout if the logged in user deleted their own record
                if (id == this.userValue?.id) {
                    this.logout();
                }
                return x;
            }));
    }

    getAllRoles() {
        return this.http.get<Role[]>(`${environment.apiUrl}/api/role/`);
    }

    getRoleById(id: string) {
        return this.http.get<Role>(`${environment.apiUrl}/api/role/${id}`);
    }

    hasNoAccess(): boolean {
        return this.userValue!.authorities?.authority == "ROLE_NO_ROLE";
    }

    hasReadAccess(): boolean {
        return this.userValue?.authorities?.authority == "ROLE_READ_ROLE" || this.hasWriteAccess() || this.hasAdminAccess();
    }

    hasWriteAccess(): boolean {
        return this.userValue?.authorities?.authority == "ROLE_WRITE_ROLE" || this.hasAdminAccess();
    }

    hasAdminAccess(): boolean {
        return this.userValue?.authorities?.authority == "ROLE_ADMIN_ROLE";
    }
}