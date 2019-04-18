import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IOrgUnitLevel } from 'app/shared/model/org-unit-level.model';
import { OrgUnitLevelService } from './org-unit-level.service';

@Component({
    selector: 'pbr-org-unit-level-update',
    templateUrl: './org-unit-level-update.component.html'
})
export class OrgUnitLevelUpdateComponent implements OnInit {
    orgUnitLevel: IOrgUnitLevel;
    isSaving: boolean;

    constructor(protected orgUnitLevelService: OrgUnitLevelService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ orgUnitLevel }) => {
            this.orgUnitLevel = orgUnitLevel;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.orgUnitLevel.id !== undefined) {
            this.subscribeToSaveResponse(this.orgUnitLevelService.update(this.orgUnitLevel));
        } else {
            this.subscribeToSaveResponse(this.orgUnitLevelService.create(this.orgUnitLevel));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrgUnitLevel>>) {
        result.subscribe((res: HttpResponse<IOrgUnitLevel>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
