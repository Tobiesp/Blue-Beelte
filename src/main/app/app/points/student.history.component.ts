import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { StudentService } from '@app/_services';
import { Student } from '@app/_models';

@Component({ templateUrl: 'student.history.component.html' })
export class StudentHistoryComponent implements OnInit {
    students?: any[];

    constructor(private studentService: StudentService) {}

    ngOnInit() {
        this.studentService.getAllStudents()
            .pipe(first())
            .subscribe(students => this.students = students);
    }

    deleteStudent(id: string | undefined) {
        if(id === undefined) {
            return;
        }
        const student = this.students!.find(x => x.id === id);
        student!.isDeleting = true;
        this.studentService.deleteStudent(id)
            .pipe(first())
            .subscribe(() => this.students = this.students);
    }
}