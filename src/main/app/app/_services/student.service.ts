import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { Student } from '@app/_models';
import { GroupService } from './group.service';

@Injectable({ providedIn: 'root' })
export class StudentService {

    constructor(
        private router: Router,
        private http: HttpClient,
        private groupService: GroupService
    ) {
        
    }

    getAllStudents() {
        return this.http.get<Student[]>(`${environment.apiUrl}/api/student/`);
    }

    getStudentById(id: string) {
        return this.http.get<Student>(`${environment.apiUrl}/api/student/${id}`);
    }

    createStudent(student: Student) {
        const group = this.groupService.getGroupByName(student.group?.name).subscribe(
            group => student.group = group
        );
        return this.http.post(`${environment.apiUrl}/api/student/`, student);
    }

    updateStudent(id: string, params: any) {
        const group = this.groupService.getGroupByName(params.group?.name).subscribe(
            group => params.group = group
        );
        return this.http.put(`${environment.apiUrl}/api/student/${id}`, params)
            .pipe(map(x => {
                return x;
            }));
    }

    deleteStudent(id: string) {
        return this.http.delete(`${environment.apiUrl}/api/student/${id}`)
            .pipe(map(x => {
                return x;
            }));
    }
}