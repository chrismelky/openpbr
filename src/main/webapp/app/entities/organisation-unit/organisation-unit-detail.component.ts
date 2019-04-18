import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrganisationUnit } from 'app/shared/model/organisation-unit.model';

@Component({
    selector: 'pbr-organisation-unit-detail',
    templateUrl: './organisation-unit-detail.component.html'
})
export class OrganisationUnitDetailComponent implements OnInit {
    organisationUnit: IOrganisationUnit;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ organisationUnit }) => {
            this.organisationUnit = organisationUnit;
        });
    }

    previousState() {
        window.history.back();
    }
}
