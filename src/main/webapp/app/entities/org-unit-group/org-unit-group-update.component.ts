import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IOrgUnitGroup } from 'app/shared/model/org-unit-group.model';
import { OrgUnitGroupService } from './org-unit-group.service';
import { IOrganisationUnit } from 'app/shared/model/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit';

@Component({
    selector: 'pbr-org-unit-group-update',
    templateUrl: './org-unit-group-update.component.html'
})
export class OrgUnitGroupUpdateComponent implements OnInit {
    orgUnitGroup: IOrgUnitGroup;
    isSaving: boolean;

    organisationunits: IOrganisationUnit[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected orgUnitGroupService: OrgUnitGroupService,
        protected organisationUnitService: OrganisationUnitService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ orgUnitGroup }) => {
            this.orgUnitGroup = orgUnitGroup;
        });
        this.organisationUnitService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IOrganisationUnit[]>) => mayBeOk.ok),
                map((response: HttpResponse<IOrganisationUnit[]>) => response.body)
            )
            .subscribe((res: IOrganisationUnit[]) => (this.organisationunits = res), (res: HttpErrorResponse) => this.onError(res.message));
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

    trackOrganisationUnitById(index: number, item: IOrganisationUnit) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
