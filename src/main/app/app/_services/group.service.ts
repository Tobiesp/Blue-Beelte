import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { Group } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class GroupService {

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        
    }

    getAllGroups() {
        return this.http.get<Group[]>(`${environment.apiUrl}/api/group/`);
    }

    getGroupById(id: string) {
        return this.http.get<Group>(`${environment.apiUrl}/api/group/${id}`);
    }

    updateGroup(id: string, params: any) {
        return this.http.put(`${environment.apiUrl}/api/group/${id}`, params)
            .pipe(map(x => {
                return x;
            }));
    }

    deleteGroup(id: string) {
        return this.http.delete(`${environment.apiUrl}/api/group/${id}`)
            .pipe(map(x => {
                return x;
            }));
    }
}