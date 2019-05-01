import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanningUnitLevel } from 'app/shared/model/planning-unit-level.model';

@Component({
    selector: 'pbr-planning-unit-level-detail',
    templateUrl: './planning-unit-level-detail.component.html'
})
export class PlanningUnitLevelDetailComponent implements OnInit {
    planningUnitLevel: IPlanningUnitLevel;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planningUnitLevel }) => {
            this.planningUnitLevel = planningUnitLevel;
        });
    }

    previousState() {
        window.history.back();
    }
}
