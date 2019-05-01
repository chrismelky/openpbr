import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanningUnitGroupSet } from 'app/shared/model/planning-unit-group-set.model';

@Component({
    selector: 'pbr-planning-unit-group-set-detail',
    templateUrl: './planning-unit-group-set-detail.component.html'
})
export class PlanningUnitGroupSetDetailComponent implements OnInit {
    planningUnitGroupSet: IPlanningUnitGroupSet;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planningUnitGroupSet }) => {
            this.planningUnitGroupSet = planningUnitGroupSet;
        });
    }

    previousState() {
        window.history.back();
    }
}
