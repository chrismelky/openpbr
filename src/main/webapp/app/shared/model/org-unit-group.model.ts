import { IOrganisationUnit } from 'app/shared/model/organisation-unit.model';
import { IAttributeValue } from 'app/shared/model/attribute-value.model';

export interface IOrgUnitGroup {
    id?: number;
    uid?: string;
    code?: string;
    name?: string;
    sortOrder?: number;
    isActive?: boolean;
    organisationUnits?: IOrganisationUnit[];
    attributeValues?: IAttributeValue[];
}

export class OrgUnitGroup implements IOrgUnitGroup {
    constructor(
        public id?: number,
        public uid?: string,
        public code?: string,
        public name?: string,
        public sortOrder?: number,
        public isActive?: boolean,
        public organisationUnits?: IOrganisationUnit[],
        public attributeValues?: IAttributeValue[]
    ) {
        this.isActive = this.isActive || false;
    }
}
