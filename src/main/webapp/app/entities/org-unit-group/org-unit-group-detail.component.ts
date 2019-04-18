import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrgUnitGroup } from 'app/shared/model/org-unit-group.model';

@Component({
    selector: 'pbr-org-unit-group-detail',
    templateUrl: './org-unit-group-detail.component.html'
})
export class OrgUnitGroupDetailComponent implements OnInit {
    orgUnitGroup: IOrgUnitGroup;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orgUnitGroup }) => {
            this.orgUnitGroup = orgUnitGroup;
        });
    }

    previousState() {
        window.history.back();
    }
}
