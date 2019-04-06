import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAttributeValue } from 'app/shared/model/attribute-value.model';
import { AttributeValueService } from './attribute-value.service';
import { IAttribute } from 'app/shared/model/attribute.model';
import { AttributeService } from 'app/entities/attribute';

@Component({
    selector: 'pbr-attribute-value-update',
    templateUrl: './attribute-value-update.component.html'
})
export class AttributeValueUpdateComponent implements OnInit {
    attributeValue: IAttributeValue;
    isSaving: boolean;

    attributes: IAttribute[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected attributeValueService: AttributeValueService,
        protected attributeService: AttributeService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ attributeValue }) => {
            this.attributeValue = attributeValue;
        });
        this.attributeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAttribute[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAttribute[]>) => response.body)
            )
            .subscribe((res: IAttribute[]) => (this.attributes = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.attributeValue.id !== undefined) {
            this.subscribeToSaveResponse(this.attributeValueService.update(this.attributeValue));
        } else {
            this.subscribeToSaveResponse(this.attributeValueService.create(this.attributeValue));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttributeValue>>) {
        result.subscribe((res: HttpResponse<IAttributeValue>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAttributeById(index: number, item: IAttribute) {
        return item.id;
    }
}
