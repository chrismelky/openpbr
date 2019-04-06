import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IOptionValue } from 'app/shared/model/option-value.model';
import { OptionValueService } from './option-value.service';
import { IOptionSet } from 'app/shared/model/option-set.model';
import { OptionSetService } from 'app/entities/option-set';

@Component({
    selector: 'pbr-option-value-update',
    templateUrl: './option-value-update.component.html'
})
export class OptionValueUpdateComponent implements OnInit {
    optionValue: IOptionValue;
    isSaving: boolean;

    optionsets: IOptionSet[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected optionValueService: OptionValueService,
        protected optionSetService: OptionSetService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ optionValue }) => {
            this.optionValue = optionValue;
        });
        this.optionSetService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IOptionSet[]>) => mayBeOk.ok),
                map((response: HttpResponse<IOptionSet[]>) => response.body)
            )
            .subscribe((res: IOptionSet[]) => (this.optionsets = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.optionValue.id !== undefined) {
            this.subscribeToSaveResponse(this.optionValueService.update(this.optionValue));
        } else {
            this.subscribeToSaveResponse(this.optionValueService.create(this.optionValue));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOptionValue>>) {
        result.subscribe((res: HttpResponse<IOptionValue>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackOptionSetById(index: number, item: IOptionSet) {
        return item.id;
    }
}
