import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { LastEventView } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class ReportService {

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        
    }

    getLastEvent() {
        return this.http.get<LastEventView>(`${environment.apiUrl}/api/report/getLastEventSnapshot`);
    }
}