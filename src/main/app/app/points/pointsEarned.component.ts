import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { startWith, map, first } from 'rxjs/operators';

import { StudentService, AlertService, PointTypeService, PointsEarnedService } from '@app/_services';
import { PointCategory, PointType, Student } from '@app/_models';

@Component({
  selector: 'pointsEarned.component',
  templateUrl: 'pointsEarned.component.html'
})
export class pointsEarnedComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  submitted = false;
  students?: Student[];
  pointTypes?: PointType[];
  pointCategories?: PointCategory[];
  studentNames?: string[];
  filteredStudents?: Observable<string[]>;
  eventDateValue?: string;

  constructor(
    private formBuilder: FormBuilder,
    private studentService: StudentService,
    private alertService: AlertService,
    private pointTypeService: PointTypeService,
    private pointsEarnedService: PointsEarnedService
  ) {

  }

  ngOnInit() {
    this.studentService.getAllStudents()
      .pipe(first())
      .subscribe(std => {
        this.students = std;
        this.studentNames = new Array(0);
        if (this.students?.length !== undefined) {
          this.studentNames = new Array(this.students?.length);
          let index = 0;
          this.students.forEach(currentValue => {
            if (currentValue.name && (this.studentNames !== undefined)) {
              this.studentNames[index] = currentValue.name || '';
            }
            index++;
          })
        }
      });

    this.form = this.formBuilder.group({
      student: ['', Validators.required],
      eventDate: ['', Validators.required],
      attended: false,
      bible: false,
      bibleVerse: false,
      bringAFriend: false,
      attentive: false,
      recallsLastWeekLesson: false
    });

    this.filteredStudents = this.form.controls['student'].valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || ''))
    );


  }

  private _filter(value: string): string[] {
    const filterValue = this._normalizeValue(value);
    if(this.studentNames) {
      const list = this.studentNames.filter(stdName => this._normalizeValue(stdName).includes(filterValue), this);
      return list;
    } else {
      return [];
    }
    
  }

  private _normalizeValue(value: string): string {
    return value.toLowerCase().replace(/\s/g, '');
  }

  // convenience getter for easy access to form fields
  get f() { return this.form.controls; }

  fireSelectedValueEvent(value: string) {
    console.log("Selected student: " + value);
    this.studentService.getStudentName(value).subscribe(s => {
      if(s && s.length > 0) {
        if(s[0].id) {
          this.pointsEarnedService.getPossibleEarnedPointByStudentId(s[0].id).subscribe(possiblePoints => {
            if(possiblePoints.eventDate){
              this.f.eventDate.setValue(this.parseDate(possiblePoints.eventDate));
            }
            possiblePoints.points?.forEach(pnt => {
              if(pnt.pointCategory?.category) {
                this.f[pnt.pointCategory?.category].enable();
              }
            })
          })
        } else {
          console.error("Student not found: " + value);
        }
      } else {
        console.error("Student not found: " + value);
      }
    });
  }

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

  parseDate(dateValue: string):Date {
    let strDate = "";
    if(!dateValue) {
      return new Date();
    }
    if(dateValue.startsWith('1')) {
      strDate = 'January';
    } else if(dateValue.startsWith('2')) {
      strDate = 'Febuary';
    } else if(dateValue.startsWith('3')) {
      strDate = 'March';
    } else if(dateValue.startsWith('4')) {
      strDate = 'April';
    } else if(dateValue.startsWith('5')) {
      strDate = 'May';
    } else if(dateValue.startsWith('6')) {
      strDate = 'June';
    } else if(dateValue.startsWith('7')) {
      strDate = 'July';
    } else if(dateValue.startsWith('8')) {
      strDate = 'August';
    } else if(dateValue.startsWith('9')) {
      strDate = 'September';
    } else if(dateValue.startsWith('10')) {
      strDate = 'October';
    } else if(dateValue.startsWith('11')) {
      strDate = 'November';
    } else if(dateValue.startsWith('12')) {
      strDate = 'December';
    }
    strDate = strDate + " " + dateValue.split('/')[1] + ', ' + dateValue.split('/')[2] + " 00:00:00 CST"
    return new Date(Date.parse(strDate));
  }
}