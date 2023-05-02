import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { Student } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class StudentService {

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        
    }

    getAllGroups() {
        return this.http.get<Student[]>(`${environment.apiUrl}/api/student/`);
    }

    getGroupById(id: string) {
        return this.http.get<Student>(`${environment.apiUrl}/api/student/${id}`);
    }

    updateGroup(id: string, params: any) {
        return this.http.put(`${environment.apiUrl}/api/student/${id}`, params)
            .pipe(map(x => {
                return x;
            }));
    }

    deleteGroup(id: string) {
        return this.http.delete(`${environment.apiUrl}/api/student/${id}`)
            .pipe(map(x => {
                return x;
            }));
    }
}