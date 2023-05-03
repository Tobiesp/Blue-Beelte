import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { RunningTotals } from '@app/_models';

@Injectable({ providedIn: 'root' })
export class RunningTotalsService {

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        
    }

    getAllTotals() {
        return this.http.get<RunningTotals[]>(`${environment.apiUrl}/api/points/total/running/`);
    }

    getTotalById(id: string) {
        return this.http.get<RunningTotals>(`${environment.apiUrl}/api/points/total/running/${id}`);
    }
}