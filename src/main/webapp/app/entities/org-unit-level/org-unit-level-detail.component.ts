import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrgUnitLevel } from 'app/shared/model/org-unit-level.model';

@Component({
    selector: 'pbr-org-unit-level-detail',
    templateUrl: './org-unit-level-detail.component.html'
})
export class OrgUnitLevelDetailComponent implements OnInit {
    orgUnitLevel: IOrgUnitLevel;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orgUnitLevel }) => {
            this.orgUnitLevel = orgUnitLevel;
        });
    }

    previousState() {
        window.history.back();
    }
}
