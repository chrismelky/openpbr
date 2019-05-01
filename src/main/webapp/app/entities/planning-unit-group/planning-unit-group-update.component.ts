import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';
import { PlanningUnitGroupService } from './planning-unit-group.service';
import { IPlanningUnit } from 'app/shared/model/planning-unit.model';
import { PlanningUnitService } from 'app/entities/planning-unit';
import { IPlanningUnitGroupSet } from 'app/shared/model/planning-unit-group-set.model';
import { PlanningUnitGroupSetService } from 'app/entities/planning-unit-group-set';

@Component({
    selector: 'pbr-planning-unit-group-update',
    templateUrl: './planning-unit-group-update.component.html'
})
export class PlanningUnitGroupUpdateComponent implements OnInit {
    planningUnitGroup: IPlanningUnitGroup;
    isSaving: boolean;

    planningunits: IPlanningUnit[];

    planningunitgroupsets: IPlanningUnitGroupSet[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected planningUnitGroupService: PlanningUnitGroupService,
        protected planningUnitService: PlanningUnitService,
        protected planningUnitGroupSetService: PlanningUnitGroupSetService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ planningUnitGroup }) => {
            this.planningUnitGroup = planningUnitGroup;
        });
        this.planningUnitService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IPlanningUnit[]>) => mayBeOk.ok),
                map((response: HttpResponse<IPlanningUnit[]>) => response.body)
            )
            .subscribe((res: IPlanningUnit[]) => (this.planningunits = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.planningUnitGroupSetService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IPlanningUnitGroupSet[]>) => mayBeOk.ok),
                map((response: HttpResponse<IPlanningUnitGroupSet[]>) => response.body)
            )
            .subscribe(
                (res: IPlanningUnitGroupSet[]) => (this.planningunitgroupsets = res),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.planningUnitGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.planningUnitGroupService.update(this.planningUnitGroup));
        } else {
            this.subscribeToSaveResponse(this.planningUnitGroupService.create(this.planningUnitGroup));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanningUnitGroup>>) {
        result.subscribe((res: HttpResponse<IPlanningUnitGroup>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPlanningUnitGroupSetById(index: number, item: IPlanningUnitGroupSet) {
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
