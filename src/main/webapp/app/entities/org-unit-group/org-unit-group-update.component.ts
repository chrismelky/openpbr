import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IOrgUnitGroup } from 'app/shared/model/org-unit-group.model';
import { OrgUnitGroupService } from './org-unit-group.service';
import { IOrganisationUnit, OrganisationUnit } from 'app/shared/model/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit';
import { Attribute } from 'app/shared/model/attribute.model';
import { AttributeValue } from 'app/shared/model/attribute-value.model';

@Component({
    selector: 'pbr-org-unit-group-update',
    templateUrl: './org-unit-group-update.component.html'
})
export class OrgUnitGroupUpdateComponent implements OnInit {
    orgUnitGroup: IOrgUnitGroup;
    isSaving: boolean;
    orgUnitSelected: IOrganisationUnit[] = [];
    orgUnitGroupAttributes: Attribute[];
    orgUnitGroupInitialAttrValues: AttributeValue[];
    attrForm = { form: { invalid: false } };

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected orgUnitGroupService: OrgUnitGroupService,
        protected activatedRoute: ActivatedRoute,
        private orgUnitService: OrganisationUnitService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ orgUnitGroup, attributes }) => {
            this.orgUnitGroup = orgUnitGroup;
            this.orgUnitGroupAttributes = [...attributes];
            this.orgUnitGroupInitialAttrValues = [...this.orgUnitGroup.attributeValues];
            if (this.orgUnitGroup.organisationUnits) {
                this.orgUnitGroup.organisationUnits.forEach((ou: OrganisationUnit) => {
                    this.orgUnitSelected.push(ou);
                });
            }
        });
    }

    onOrgSelectedChange(ous: OrganisationUnit[]) {
        this.orgUnitGroup.organisationUnits = ous;
    }

    onAttrValueChange(attrValues) {
        this.orgUnitGroup.attributeValues = attrValues.data;
        this.attrForm = attrValues.form;
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.orgUnitGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.orgUnitGroupService.update(this.orgUnitGroup));
        } else {
            this.subscribeToSaveResponse(this.orgUnitGroupService.create(this.orgUnitGroup));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrgUnitGroup>>) {
        result.subscribe((res: HttpResponse<IOrgUnitGroup>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
