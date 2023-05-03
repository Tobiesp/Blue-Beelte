import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { PointType } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class PointTypeService {

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        
    }

    getAllGroups() {
        return this.http.get<PointType[]>(`${environment.apiUrl}/api/points/config/`);
    }

    getGroupById(id: string) {
        return this.http.get<PointType>(`${environment.apiUrl}/api/points/config/${id}`);
    }

    updateGroup(id: string, params: any) {
        return this.http.put(`${environment.apiUrl}/api/points/config/${id}`, params)
            .pipe(map(x => {
                return x;
            }));
    }

    deleteGroup(id: string) {
        return this.http.delete(`${environment.apiUrl}/api/points/config/${id}`)
            .pipe(map(x => {
                return x;
            }));
    }
}