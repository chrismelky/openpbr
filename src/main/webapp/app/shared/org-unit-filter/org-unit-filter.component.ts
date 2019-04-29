import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { OrgUnitLevelService } from 'app/entities/org-unit-level';
import { OrgUnitLevel } from 'app/shared/model/org-unit-level.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit';
import { IOrganisationUnit, OrganisationUnit } from 'app/shared/model/organisation-unit.model';
import { SelectionModel } from '@angular/cdk/collections';

@Component({
    selector: 'pbr-org-unit-filter',
    templateUrl: './org-unit-filter.component.html',
    styles: []
})
export class OrgUnitFilterComponent implements OnInit {
    @Input() selectedOrgUnits: IOrganisationUnit[];
    @Input() multiple: Boolean;
    @Output() onOrgSelectedChange: EventEmitter<any> = new EventEmitter<any>();
    orgUnitLevels: OrgUnitLevel[];
    userLevel = 1;
    orgUnits = [];

    constructor(private orgUnitLevelService: OrgUnitLevelService, private orgUnitService: OrganisationUnitService) {}

    ngOnInit() {
        if (this.selectedOrgUnits === null) {
            this.selectedOrgUnits = [];
        }
        this.orgUnitLevelService
            .query({ 'level.greaterOrEqualThan': this.userLevel })
            .pipe(
                filter((response: HttpResponse<OrgUnitLevel[]>) => response.ok),
                map((levels: HttpResponse<OrgUnitLevel[]>) => levels.body)
            )
            .subscribe(levels => {
                this.orgUnitLevels = levels;
                this.getOrgUnits(this.orgUnitLevels[0].level, null);
            });
    }

    getOrgUnits(level, parentId) {
        this.clearChild(level);
        const opt = parentId !== null ? { 'parentId.equals': parentId } : { 'level.equals': level };
        this.orgUnitService
            .query(opt)
            .pipe(
                filter((response: HttpResponse<IOrganisationUnit[]>) => response.ok),
                map((ou: HttpResponse<IOrganisationUnit[]>) => ou.body)
            )
            .subscribe((ous: IOrganisationUnit[]) => {
                this.orgUnits[level] = ous;
            });
    }

    clearChild(level) {
        for (let i = level + 1; i <= this.orgUnitLevels.length; i++) {
            this.orgUnits[i] = [];
        }
    }

    toggleOrgUnit(ou: OrganisationUnit) {
        if (!this.multiple) {
            this.selectedOrgUnits.length = 0;
            this.selectedOrgUnits.push(ou);
            this.onOrgSelectedChange.emit(this.selectedOrgUnits);
            return;
        }
        const idx = this.selectedOrgUnits.map(i => i.id).indexOf(ou.id);
        if (idx !== -1) {
            this.selectedOrgUnits.splice(idx, 1);
        } else {
            this.selectedOrgUnits.push(ou);
        }
        this.onOrgSelectedChange.emit(this.selectedOrgUnits);
    }

    isSelected(ou: OrganisationUnit) {
        const idx = this.selectedOrgUnits.map(i => i.id).indexOf(ou.id);
        return idx !== -1;
    }

    trackOuBy(index, item) {
        return item.id;
    }
}
