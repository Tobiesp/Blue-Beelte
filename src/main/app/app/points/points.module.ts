import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '.././material.module';

import { PointsRoutingModule } from './points-routing.module';
import { LayoutComponent } from './layout.component';
import { pointsEarnedComponent } from './pointsEarned.component';
import { PointsSpentComponent } from './pointsSpent.component';
import { PointsHistoryComponent } from './history.component';
import { StudentHistoryComponent } from './student.history.component'

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        PointsRoutingModule,
        MaterialModule
    ],
    declarations: [
        LayoutComponent,
        pointsEarnedComponent,
        PointsSpentComponent,
        PointsHistoryComponent,
        StudentHistoryComponent
    ]
})
export class PointsModule { }