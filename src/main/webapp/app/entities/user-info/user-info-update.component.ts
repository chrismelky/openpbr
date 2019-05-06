import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IUserInfo } from 'app/shared/model/user-info.model';
import { UserInfoService } from './user-info.service';
import { IUser, UserService } from 'app/core';
import { IAttributeValue } from 'app/shared/model/attribute-value.model';
import { AttributeValueService } from 'app/entities/attribute-value';
import { IOrganisationUnit } from 'app/shared/model/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit';
import { IPlanningUnit } from 'app/shared/model/planning-unit.model';
import { PlanningUnitService } from 'app/entities/planning-unit';

@Component({
    selector: 'pbr-user-info-update',
    templateUrl: './user-info-update.component.html'
})
export class UserInfoUpdateComponent implements OnInit {
    userInfo: IUserInfo;
    isSaving: boolean;

    users: IUser[];

    attributevalues: IAttributeValue[];

    organisationunits: IOrganisationUnit[];

    planningunits: IPlanningUnit[];
    birthDayDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected userInfoService: UserInfoService,
        protected userService: UserService,
        protected attributeValueService: AttributeValueService,
        protected organisationUnitService: OrganisationUnitService,
        protected planningUnitService: PlanningUnitService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userInfo }) => {
            this.userInfo = userInfo;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.attributeValueService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAttributeValue[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAttributeValue[]>) => response.body)
            )
            .subscribe((res: IAttributeValue[]) => (this.attributevalues = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.organisationUnitService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IOrganisationUnit[]>) => mayBeOk.ok),
                map((response: HttpResponse<IOrganisationUnit[]>) => response.body)
            )
            .subscribe((res: IOrganisationUnit[]) => (this.organisationunits = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.planningUnitService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IPlanningUnit[]>) => mayBeOk.ok),
                map((response: HttpResponse<IPlanningUnit[]>) => response.body)
            )
            .subscribe((res: IPlanningUnit[]) => (this.planningunits = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.userInfo.id !== undefined) {
            this.subscribeToSaveResponse(this.userInfoService.update(this.userInfo));
        } else {
            this.subscribeToSaveResponse(this.userInfoService.create(this.userInfo));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserInfo>>) {
        result.subscribe((res: HttpResponse<IUserInfo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackAttributeValueById(index: number, item: IAttributeValue) {
        return item.id;
    }

    trackOrganisationUnitById(index: number, item: IOrganisationUnit) {
        return item.id;
    }

    trackPlanningUnitById(index: number, item: IPlanningUnit) {
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
