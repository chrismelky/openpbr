import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAttribute } from 'app/shared/model/attribute.model';
import { AttributeService } from './attribute.service';
import { IOptionSet } from 'app/shared/model/option-set.model';
import { OptionSetService } from 'app/entities/option-set';

@Component({
    selector: 'pbr-attribute-update',
    templateUrl: './attribute-update.component.html'
})
export class AttributeUpdateComponent implements OnInit {
    attribute: IAttribute;
    isSaving: boolean;

    optionsets: IOptionSet[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected attributeService: AttributeService,
        protected optionSetService: OptionSetService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ attribute }) => {
            this.attribute = attribute;
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
        if (this.attribute.id !== undefined) {
            this.subscribeToSaveResponse(this.attributeService.update(this.attribute));
        } else {
            this.subscribeToSaveResponse(this.attributeService.create(this.attribute));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttribute>>) {
        result.subscribe((res: HttpResponse<IAttribute>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
