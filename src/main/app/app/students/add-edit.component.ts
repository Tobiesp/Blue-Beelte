import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { Group } from '@app/_models';
import { StudentService, GroupService, AlertService } from '@app/_services';

@Component({ templateUrl: 'add-edit.component.html' })
export class AddEditComponent implements OnInit {
    form!: FormGroup;
    id?: string;
    title!: string;
    loading = false;
    submitting = false;
    submitted = false;
    groups?: Group[];

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private studentService: StudentService,
        private groupService: GroupService,
        private alertService: AlertService
    ) { }

    ngOnInit() {
        this.id = this.route.snapshot.params['id'];

        this.groupService.getAllGroups()
            .pipe(first())
            .subscribe(groups => this.groups = groups);

        // form with validation rules
        this.form = this.formBuilder.group({
            name: ['', Validators.required],
            group: ['', Validators.required],
            grade: ['', Validators.required]
        });

        this.title = 'Add Student';
        if (this.id) {
            // edit mode
            this.title = 'Edit Student';
            this.loading = true;
            this.studentService.getStudentById(this.id)
                .pipe(first())
                .subscribe(x => {
                    this.form.patchValue(x);
                    this.loading = false;
                });
        }
    }

    // convenience getter for easy access to form fields
    get f() { return this.form.controls; }

    onSubmit() {
        this.submitted = true;

        // reset alerts on submit
        this.alertService.clear();

        // stop here if form is invalid
        if (this.form.invalid) {
            return;
        }

        this.submitting = true;
        this.saveStudent()
            .pipe(first())
            .subscribe({
                next: () => {
                    this.alertService.success('Student saved', { keepAfterRouteChange: true });
                    this.router.navigateByUrl('/student');
                },
                error: error => {
                    this.alertService.error(error);
                    this.submitting = false;
                }
            })
    }

    private saveStudent() {
        // create or update user based on id param
        return this.id
            ? this.studentService.updateStudent(this.id!, this.form.value)
            : this.studentService.createStudent(this.form.value);
    }
}