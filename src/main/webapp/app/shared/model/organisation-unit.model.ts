import { Moment } from 'moment';
import { IOrganisationUnit } from 'app/shared/model/organisation-unit.model';

export interface IOrganisationUnit {
    id?: number;
    uid?: string;
    code?: string;
    name?: string;
    level?: number;
    openingDate?: Moment;
    closedDate?: Moment;
    url?: string;
    latitude?: number;
    longitude?: number;
    address?: string;
    email?: string;
    phoneNumner?: string;
    sortOrder?: number;
    isActive?: boolean;
    parent?: IOrganisationUnit;
}

export class OrganisationUnit implements IOrganisationUnit {
    constructor(
        public id?: number,
        public uid?: string,
        public code?: string,
        public name?: string,
        public level?: number,
        public openingDate?: Moment,
        public closedDate?: Moment,
        public url?: string,
        public latitude?: number,
        public longitude?: number,
        public address?: string,
        public email?: string,
        public phoneNumner?: string,
        public sortOrder?: number,
        public isActive?: boolean,
        public parent?: IOrganisationUnit
    ) {
        this.isActive = this.isActive || false;
    }
}
