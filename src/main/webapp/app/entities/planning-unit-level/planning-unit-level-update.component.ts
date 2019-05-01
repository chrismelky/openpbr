import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IPlanningUnitLevel } from 'app/shared/model/planning-unit-level.model';
import { PlanningUnitLevelService } from './planning-unit-level.service';

@Component({
    selector: 'pbr-planning-unit-level-update',
    templateUrl: './planning-unit-level-update.component.html'
})
export class PlanningUnitLevelUpdateComponent implements OnInit {
    planningUnitLevel: IPlanningUnitLevel;
    isSaving: boolean;

    constructor(protected planningUnitLevelService: PlanningUnitLevelService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ planningUnitLevel }) => {
            this.planningUnitLevel = planningUnitLevel;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.planningUnitLevel.id !== undefined) {
            this.subscribeToSaveResponse(this.planningUnitLevelService.update(this.planningUnitLevel));
        } else {
            this.subscribeToSaveResponse(this.planningUnitLevelService.create(this.planningUnitLevel));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanningUnitLevel>>) {
        result.subscribe((res: HttpResponse<IPlanningUnitLevel>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
