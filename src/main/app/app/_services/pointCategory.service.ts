import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { PointCategory } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class PointCategoryService {

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        
    }

    getAllGroups() {
        return this.http.get<PointCategory[]>(`${environment.apiUrl}/api/category/`);
    }

    getGroupById(id: string) {
        return this.http.get<PointCategory>(`${environment.apiUrl}/api/category/${id}`);
    }

    updateGroup(id: string, params: any) {
        return this.http.put(`${environment.apiUrl}/api/category/${id}`, params)
            .pipe(map(x => {
                return x;
            }));
    }

    deleteGroup(id: string) {
        return this.http.delete(`${environment.apiUrl}/api/category/${id}`)
            .pipe(map(x => {
                return x;
            }));
    }
}