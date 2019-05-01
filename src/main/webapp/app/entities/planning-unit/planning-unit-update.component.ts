import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPlanningUnit } from 'app/shared/model/planning-unit.model';
import { PlanningUnitService } from './planning-unit.service';
import { IPlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';
import { PlanningUnitGroupService } from 'app/entities/planning-unit-group';

@Component({
    selector: 'pbr-planning-unit-update',
    templateUrl: './planning-unit-update.component.html'
})
export class PlanningUnitUpdateComponent implements OnInit {
    planningUnit: IPlanningUnit;
    isSaving: boolean;

    parents: IPlanningUnit[];

    planningunitgroups: IPlanningUnitGroup[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected planningUnitService: PlanningUnitService,
        protected planningUnitGroupService: PlanningUnitGroupService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ planningUnit }) => {
            this.planningUnit = planningUnit;
        });
        this.planningUnitService
            .query({ 'planningUnitId.specified': 'false' })
            .pipe(
                filter((mayBeOk: HttpResponse<IPlanningUnit[]>) => mayBeOk.ok),
                map((response: HttpResponse<IPlanningUnit[]>) => response.body)
            )
            .subscribe(
                (res: IPlanningUnit[]) => {
                    if (!this.planningUnit.parent || !this.planningUnit.parent.id) {
                        this.parents = res;
                    } else {
                        this.planningUnitService
                            .find(this.planningUnit.parent.id)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IPlanningUnit>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IPlanningUnit>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IPlanningUnit) => (this.parents = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.planningUnitGroupService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IPlanningUnitGroup[]>) => mayBeOk.ok),
                map((response: HttpResponse<IPlanningUnitGroup[]>) => response.body)
            )
            .subscribe(
                (res: IPlanningUnitGroup[]) => (this.planningunitgroups = res),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.planningUnit.id !== undefined) {
            this.subscribeToSaveResponse(this.planningUnitService.update(this.planningUnit));
        } else {
            this.subscribeToSaveResponse(this.planningUnitService.create(this.planningUnit));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanningUnit>>) {
        result.subscribe((res: HttpResponse<IPlanningUnit>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPlanningUnitById(index: number, item: IPlanningUnit) {
        return item.id;
    }

    trackPlanningUnitGroupById(index: number, item: IPlanningUnitGroup) {
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
