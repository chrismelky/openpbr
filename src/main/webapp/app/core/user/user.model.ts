import { Gender } from 'app/shared/model/user-info.model';
import { Moment } from 'moment';
import { IAttributeValue } from 'app/shared/model/attribute-value.model';
import { IOrganisationUnit, OrganisationUnit } from 'app/shared/model/organisation-unit.model';
import { IPlanningUnit } from 'app/shared/model/planning-unit.model';

export interface IUser {
    id?: any;
    login?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    phoneNumber?: string;
    jobTitle?: string;
    introduction?: string;
    gender?: Gender;
    birthDay?: Moment;
    nationality?: string;
    employer?: string;
    education?: string;
    activated?: boolean;
    langKey?: string;
    authorities?: any[];
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    password?: string;
    attributeValues?: IAttributeValue[];
    organisationUnit?: IOrganisationUnit;
    planningUnit?: IPlanningUnit;
}

export class User implements IUser {
    constructor(
        public id?: any,
        public login?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public phoneNumber?: string,
        public jobTitle?: string,
        public introduction?: string,
        public gender?: Gender,
        public birthDay?: Moment,
        public nationality?: string,
        public employer?: string,
        public education?: string,
        public activated?: boolean,
        public langKey?: string,
        public authorities?: any[],
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date,
        public password?: string,
        public attributeValues?: IAttributeValue[],
        public organisationUnit?: IOrganisationUnit,
        public planningUnit?: IPlanningUnit
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.firstName = firstName ? firstName : null;
        this.lastName = lastName ? lastName : null;
        this.email = email ? email : null;
        this.phoneNumber = phoneNumber ? phoneNumber : null;
        this.jobTitle = jobTitle ? jobTitle : null;
        this.introduction = introduction ? introduction : null;
        this.gender = gender ? gender : null;
        this.birthDay = birthDay ? birthDay : null;
        this.nationality = nationality ? nationality : null;
        this.employer = employer ? employer : null;
        this.education = education ? employer : null;
        this.activated = activated ? activated : false;
        this.langKey = langKey ? langKey : null;
        this.authorities = authorities ? authorities : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.password = password ? password : null;
        this.attributeValues = attributeValues ? attributeValues : [];
        this.organisationUnit = organisationUnit ? organisationUnit : null;
        this.planningUnit = planningUnit ? planningUnit : null;
    }
}
