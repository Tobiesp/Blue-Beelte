import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { startWith, map, first } from 'rxjs/operators';

import { StudentService, AlertService } from '@app/_services';
import { Student } from '@app/_models';

@Component({ 
    selector: 'pointsEarned.component',
    templateUrl: 'pointsEarned.component.html' })
export class pointsEarnedComponent implements OnInit {
    form!: FormGroup;
    loading = false;
    submitted = false;
    students?: Student[];
    studentNames: string[] = [];
    filteredStudents?: Observable<string[]>;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private studentService: StudentService,
        private alertService: AlertService
    ) { 

    }

    ngOnInit() {
        this.studentService.getAllStudents()
            .pipe(first())
            .subscribe(std => this.students = std);
        this.studentNames = new Array(0);
        if(this.students?.length !== undefined) {
            this.studentNames = new Array(this.students?.length);
            this.students.forEach((currentValue, index) => {
                if(!currentValue.name) {
                    this.studentNames[index] = currentValue.name || '';
                }
              })
        }
        
        this.form = this.formBuilder.group({
            student: ['', Validators.required],
            eventDate: ['', Validators.required]
            //Add all the check boxes fro different points
        });
        this.filteredStudents = this.form.controls['student'].valueChanges.pipe(
            startWith(''),
            map(value => this._filter(value || ''))
          );
    }

    private _filter(value: string): string[] {
      const filterValue = this._normalizeValue(value);
      return this.studentNames.filter(studentName => this._normalizeValue(studentName).includes(filterValue));
    }
  
    private _normalizeValue(value: string): string {
      return value.toLowerCase().replace(/\s/g, '');
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

        this.loading = true;
        //Add submition logic so that the on successful submit form resets
        // this.accountService.login(this.f.username.value, this.f.password.value)
        //     .pipe(first())
        //     .subscribe({
        //         next: () => {
        //             // get return url from query parameters or default to home page
        //             const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
        //             this.router.navigateByUrl(returnUrl);
        //         },
        //         error: error => {
        //             this.alertService.error(error);
        //             this.loading = false;
        //         }
        //     });
    }
}