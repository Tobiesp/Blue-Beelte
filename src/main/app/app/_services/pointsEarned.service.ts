import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { PointsEarned, PointsEarnedCollection } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class PointsEarnedService {

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        
    }

    getAllEarnedPoints() {
        return this.http.get<PointsEarned[]>(`${environment.apiUrl}/api/points/earned/`);
    }

    getEarnedPointById(id: string) {
        return this.http.get<PointsEarned>(`${environment.apiUrl}/api/points/earned/${id}`);
    }

    getPossibleEarnedPointByStudentId(stdId: string) {
        return this.http.get<PointsEarnedCollection>(`${environment.apiUrl}/api/points/earned/getPossiblePoints?studentId=${stdId}`);
    }

    updateEarnedPoint(id: string, params: any) {
        return this.http.put(`${environment.apiUrl}/api/points/earned/${id}`, params)
            .pipe(map(x => {
                return x;
            }));
    }

    deleteEarnedPoint(id: string) {
        return this.http.delete(`${environment.apiUrl}/api/points/earned/${id}`)
            .pipe(map(x => {
                return x;
            }));
    }
}