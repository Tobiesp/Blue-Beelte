import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { StudentService } from '@app/_services';
import { Student } from '@app/_models';

@Component({ templateUrl: 'history.component.html' })
export class PointsHistoryComponent implements OnInit {
    students?: any[];

    constructor(private studentService: StudentService) {}

    ngOnInit() {
        this.studentService.getAllStudents()
            .pipe(first())
            .subscribe(students => this.students = students);
    }
}