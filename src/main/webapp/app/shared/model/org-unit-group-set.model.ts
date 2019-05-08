import { IOrgUnitGroup } from 'app/shared/model/org-unit-group.model';
import { IAttributeValue } from 'app/shared/model/attribute-value.model';

export interface IOrgUnitGroupSet {
    id?: number;
    uid?: string;
    code?: string;
    name?: string;
    description?: string;
    sortOrder?: number;
    isActive?: boolean;
    orgUnitGroups?: IOrgUnitGroup[];
    attributeValues?: IAttributeValue[];
}

export class OrgUnitGroupSet implements IOrgUnitGroupSet {
    constructor(
        public id?: number,
        public uid?: string,
        public code?: string,
        public name?: string,
        public description?: string,
        public sortOrder?: number,
        public isActive?: boolean,
        public orgUnitGroups?: IOrgUnitGroup[],
        public attributeValues?: IAttributeValue[]
    ) {
        this.isActive = this.isActive || false;
    }
}
