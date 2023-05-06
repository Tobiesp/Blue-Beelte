import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LayoutComponent } from './layout.component';
import { pointsEarnedComponent } from './pointsEarned.component';
import { PointsSpentComponent } from './pointsSpent.component';
import { PointsHistoryComponent } from './history.component';
import { StudentHistoryComponent } from './student.history.component';

const routes: Routes = [
    {
        path: '', component: LayoutComponent,
        children: [
            { path: 'earned/add', component: pointsEarnedComponent },
            { path: 'earned/edit/:id', component: pointsEarnedComponent },
            { path: 'spent/add', component: PointsSpentComponent },
            { path: 'spent/edit/:id', component: PointsSpentComponent },
            { path: 'history', component: PointsHistoryComponent },
            { path: 'history/:id', component: StudentHistoryComponent }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PointsRoutingModule { }