import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrgUnitGroupSet } from 'app/shared/model/org-unit-group-set.model';

@Component({
    selector: 'pbr-org-unit-group-set-detail',
    templateUrl: './org-unit-group-set-detail.component.html'
})
export class OrgUnitGroupSetDetailComponent implements OnInit {
    orgUnitGroupSet: IOrgUnitGroupSet;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orgUnitGroupSet }) => {
            this.orgUnitGroupSet = orgUnitGroupSet;
        });
    }

    previousState() {
        window.history.back();
    }
}
