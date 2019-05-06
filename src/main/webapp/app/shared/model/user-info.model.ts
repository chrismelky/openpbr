import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IAttributeValue } from 'app/shared/model/attribute-value.model';
import { IOrganisationUnit } from 'app/shared/model/organisation-unit.model';
import { IPlanningUnit } from 'app/shared/model/planning-unit.model';

export const enum Gender {
    FEMALE = 'FEMALE',
    MALE = 'MALE',
    OTHERS = 'OTHERS'
}

export interface IUserInfo {
    id?: number;
    uid?: string;
    code?: string;
    lastName?: string;
    firstName?: string;
    email?: string;
    phoneNumber?: string;
    jobTitle?: string;
    introduction?: string;
    gender?: Gender;
    birthDay?: Moment;
    nationality?: string;
    employer?: string;
    education?: string;
    user?: IUser;
    attributeValues?: IAttributeValue[];
    organisationUnit?: IOrganisationUnit;
    planningUnit?: IPlanningUnit;
}

export class UserInfo implements IUserInfo {
    constructor(
        public id?: number,
        public uid?: string,
        public code?: string,
        public lastName?: string,
        public firstName?: string,
        public email?: string,
        public phoneNumber?: string,
        public jobTitle?: string,
        public introduction?: string,
        public gender?: Gender,
        public birthDay?: Moment,
        public nationality?: string,
        public employer?: string,
        public education?: string,
        public user?: IUser,
        public attributeValues?: IAttributeValue[],
        public organisationUnit?: IOrganisationUnit,
        public planningUnit?: IPlanningUnit
    ) {}
}
