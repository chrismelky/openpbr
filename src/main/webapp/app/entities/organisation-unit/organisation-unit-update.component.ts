import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IOrganisationUnit } from 'app/shared/model/organisation-unit.model';
import { OrganisationUnitService } from './organisation-unit.service';

@Component({
    selector: 'pbr-organisation-unit-update',
    templateUrl: './organisation-unit-update.component.html'
})
export class OrganisationUnitUpdateComponent implements OnInit {
    organisationUnit: IOrganisationUnit;
    isSaving: boolean;

    parents: IOrganisationUnit[];
    openingDateDp: any;
    closedDateDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected organisationUnitService: OrganisationUnitService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ organisationUnit }) => {
            this.organisationUnit = organisationUnit;
        });
        this.organisationUnitService
            .query({ 'organisationUnitId.specified': 'false' })
            .pipe(
                filter((mayBeOk: HttpResponse<IOrganisationUnit[]>) => mayBeOk.ok),
                map((response: HttpResponse<IOrganisationUnit[]>) => response.body)
            )
            .subscribe(
                (res: IOrganisationUnit[]) => {
                    if (!this.organisationUnit.parent || !this.organisationUnit.parent.id) {
                        this.parents = res;
                    } else {
                        this.organisationUnitService
                            .find(this.organisationUnit.parent.id)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IOrganisationUnit>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IOrganisationUnit>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IOrganisationUnit) => (this.parents = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.organisationUnit.id !== undefined) {
            this.subscribeToSaveResponse(this.organisationUnitService.update(this.organisationUnit));
        } else {
            this.subscribeToSaveResponse(this.organisationUnitService.create(this.organisationUnit));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganisationUnit>>) {
        result.subscribe((res: HttpResponse<IOrganisationUnit>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
