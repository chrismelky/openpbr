import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanningUnit } from 'app/shared/model/planning-unit.model';

@Component({
    selector: 'pbr-planning-unit-detail',
    templateUrl: './planning-unit-detail.component.html'
})
export class PlanningUnitDetailComponent implements OnInit {
    planningUnit: IPlanningUnit;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planningUnit }) => {
            this.planningUnit = planningUnit;
        });
    }

    previousState() {
        window.history.back();
    }
}
