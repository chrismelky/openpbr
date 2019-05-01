import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';

@Component({
    selector: 'pbr-planning-unit-group-detail',
    templateUrl: './planning-unit-group-detail.component.html'
})
export class PlanningUnitGroupDetailComponent implements OnInit {
    planningUnitGroup: IPlanningUnitGroup;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planningUnitGroup }) => {
            this.planningUnitGroup = planningUnitGroup;
        });
    }

    previousState() {
        window.history.back();
    }
}
