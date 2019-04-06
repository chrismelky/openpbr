import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IOptionSet } from 'app/shared/model/option-set.model';
import { OptionSetService } from './option-set.service';

@Component({
    selector: 'pbr-option-set-update',
    templateUrl: './option-set-update.component.html'
})
export class OptionSetUpdateComponent implements OnInit {
    optionSet: IOptionSet;
    isSaving: boolean;

    constructor(protected optionSetService: OptionSetService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ optionSet }) => {
            this.optionSet = optionSet;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.optionSet.id !== undefined) {
            this.subscribeToSaveResponse(this.optionSetService.update(this.optionSet));
        } else {
            this.subscribeToSaveResponse(this.optionSetService.create(this.optionSet));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOptionSet>>) {
        result.subscribe((res: HttpResponse<IOptionSet>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
