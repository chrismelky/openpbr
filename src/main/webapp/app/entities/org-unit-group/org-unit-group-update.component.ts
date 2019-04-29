import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IOrgUnitGroup } from 'app/shared/model/org-unit-group.model';
import { OrgUnitGroupService } from './org-unit-group.service';
import { IOrganisationUnit, OrganisationUnit } from 'app/shared/model/organisation-unit.model';

@Component({
    selector: 'pbr-org-unit-group-update',
    templateUrl: './org-unit-group-update.component.html'
})
export class OrgUnitGroupUpdateComponent implements OnInit {
    orgUnitGroup: IOrgUnitGroup;
    isSaving: boolean;
    orgUnitSelected: IOrganisationUnit[] = [];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected orgUnitGroupService: OrgUnitGroupService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ orgUnitGroup }) => {
            this.orgUnitGroup = orgUnitGroup;
            if (this.orgUnitGroup.organisationUnits) {
                this.orgUnitGroup.organisationUnits.forEach((ou: OrganisationUnit) => {
                    this.orgUnitSelected.push(ou);
                });
            }
        });
    }

    onOrgSelectedChange(ous: OrganisationUnit[]) {
        this.orgUnitGroup.organisationUnits = ous;
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.orgUnitGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.orgUnitGroupService.update(this.orgUnitGroup));
        } else {
            this.subscribeToSaveResponse(this.orgUnitGroupService.create(this.orgUnitGroup));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrgUnitGroup>>) {
        result.subscribe((res: HttpResponse<IOrgUnitGroup>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
