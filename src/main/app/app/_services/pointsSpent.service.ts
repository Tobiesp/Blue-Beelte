import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { PointsSpent } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class PointsSpentService {

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        
    }

    getAllGroups() {
        return this.http.get<PointsSpent[]>(`${environment.apiUrl}/api/points/spent/`);
    }

    getGroupById(id: string) {
        return this.http.get<PointsSpent>(`${environment.apiUrl}/api/points/spent/${id}`);
    }

    updateGroup(id: string, params: any) {
        return this.http.put(`${environment.apiUrl}/api/points/spent/${id}`, params)
            .pipe(map(x => {
                return x;
            }));
    }

    deleteGroup(id: string) {
        return this.http.delete(`${environment.apiUrl}/api/points/spent/${id}`)
            .pipe(map(x => {
                return x;
            }));
    }
}