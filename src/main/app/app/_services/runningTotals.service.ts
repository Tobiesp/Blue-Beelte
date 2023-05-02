import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { RunningTotals } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class RunningTotalsService {

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        
    }

    getAllGroups() {
        return this.http.get<RunningTotals[]>(`${environment.apiUrl}/api/points/total/running/`);
    }

    getGroupById(id: string) {
        return this.http.get<RunningTotals>(`${environment.apiUrl}/api/points/total/running/${id}`);
    }

    updateGroup(id: string, params: any) {
        return this.http.put(`${environment.apiUrl}/api/points/total/running/${id}`, params)
            .pipe(map(x => {
                return x;
            }));
    }

    deleteGroup(id: string) {
        return this.http.delete(`${environment.apiUrl}/api/points/total/running/${id}`)
            .pipe(map(x => {
                return x;
            }));
    }
}