import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPlanningUnitGroupSet } from 'app/shared/model/planning-unit-group-set.model';
import { PlanningUnitGroupSetService } from './planning-unit-group-set.service';
import { IPlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';
import { PlanningUnitGroupService } from 'app/entities/planning-unit-group';

@Component({
    selector: 'pbr-planning-unit-group-set-update',
    templateUrl: './planning-unit-group-set-update.component.html'
})
export class PlanningUnitGroupSetUpdateComponent implements OnInit {
    planningUnitGroupSet: IPlanningUnitGroupSet;
    isSaving: boolean;

    planningunitgroups: IPlanningUnitGroup[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected planningUnitGroupSetService: PlanningUnitGroupSetService,
        protected planningUnitGroupService: PlanningUnitGroupService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ planningUnitGroupSet }) => {
            this.planningUnitGroupSet = planningUnitGroupSet;
        });
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
        if (this.planningUnitGroupSet.id !== undefined) {
            this.subscribeToSaveResponse(this.planningUnitGroupSetService.update(this.planningUnitGroupSet));
        } else {
            this.subscribeToSaveResponse(this.planningUnitGroupSetService.create(this.planningUnitGroupSet));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanningUnitGroupSet>>) {
        result.subscribe(
            (res: HttpResponse<IPlanningUnitGroupSet>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
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
