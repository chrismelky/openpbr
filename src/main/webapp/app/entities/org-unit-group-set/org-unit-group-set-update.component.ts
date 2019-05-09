import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IOrgUnitGroupSet } from 'app/shared/model/org-unit-group-set.model';
import { OrgUnitGroupSetService } from './org-unit-group-set.service';
import { IOrgUnitGroup } from 'app/shared/model/org-unit-group.model';
import { OrgUnitGroupService } from 'app/entities/org-unit-group';
import { AttributeValue, IAttributeValue } from 'app/shared/model/attribute-value.model';
import { AttributeValueService } from 'app/entities/attribute-value';
import { Attribute } from 'app/shared/model/attribute.model';

@Component({
    selector: 'pbr-org-unit-group-set-update',
    templateUrl: './org-unit-group-set-update.component.html'
})
export class OrgUnitGroupSetUpdateComponent implements OnInit {
    orgUnitGroupSet: IOrgUnitGroupSet;
    isSaving: boolean;
    orgunitgroups: IOrgUnitGroup[];
    attributevalues: IAttributeValue[];
    attrForm = { form: { invalid: false } };
    orgUnitGroupSetAttributes: Attribute[];
    orgUnitGroupSetInitialAttrValues: AttributeValue[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected orgUnitGroupSetService: OrgUnitGroupSetService,
        protected orgUnitGroupService: OrgUnitGroupService,
        protected attributeValueService: AttributeValueService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ orgUnitGroupSet, attributes }) => {
            this.orgUnitGroupSet = orgUnitGroupSet;
            this.orgUnitGroupSetAttributes = [...attributes];
            this.orgUnitGroupSetInitialAttrValues = [...this.orgUnitGroupSet.attributeValues];
        });
        this.orgUnitGroupService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IOrgUnitGroup[]>) => mayBeOk.ok),
                map((response: HttpResponse<IOrgUnitGroup[]>) => response.body)
            )
            .subscribe((res: IOrgUnitGroup[]) => (this.orgunitgroups = res), (res: HttpErrorResponse) => this.onError(res.message));
        //TODO  remove
        // this.attributeValueService
        //     .query()
        //     .pipe(
        //         filter((mayBeOk: HttpResponse<IAttributeValue[]>) => mayBeOk.ok),
        //         map((response: HttpResponse<IAttributeValue[]>) => response.body)
        //     )
        //     .subscribe((res: IAttributeValue[]) => (this.attributevalues = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.orgUnitGroupSet.id !== undefined) {
            this.subscribeToSaveResponse(this.orgUnitGroupSetService.update(this.orgUnitGroupSet));
        } else {
            this.subscribeToSaveResponse(this.orgUnitGroupSetService.create(this.orgUnitGroupSet));
        }
    }

    onGroupChanged(groups) {
        this.orgUnitGroupSet.orgUnitGroups = groups;
    }

    onAttrValueChange(attrValues) {
        this.orgUnitGroupSet.attributeValues = attrValues.data;
        this.attrForm = attrValues.form;
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrgUnitGroupSet>>) {
        result.subscribe((res: HttpResponse<IOrgUnitGroupSet>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackOrgUnitGroupById(index: number, item: IOrgUnitGroup) {
        return item.id;
    }

    trackAttributeValueById(index: number, item: IAttributeValue) {
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
