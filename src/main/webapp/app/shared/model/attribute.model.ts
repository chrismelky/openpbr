import { IOptionSet } from 'app/shared/model/option-set.model';

export const enum ValueType {
    TEXT = 'TEXT',
    LONG_TEXT = 'LONG_TEXT',
    LETTER = 'LETTER',
    PHONE_NUMBER = 'PHONE_NUMBER',
    EMAIL = 'EMAIL',
    YES_NO = 'YES_NO',
    YES_ONLY = 'YES_ONLY',
    DATE = 'DATE',
    DATE_TIME = 'DATE_TIME',
    TIME = 'TIME',
    NUMBER = 'NUMBER',
    UNIT_INTERVAL = 'UNIT_INTERVAL',
    PERCENTAGE = 'PERCENTAGE',
    INTEGER = 'INTEGER',
    POSITIVE_INTEGER = 'POSITIVE_INTEGER',
    NEGATIVE_INTEGER = 'NEGATIVE_INTEGER',
    POSITIVE_OR_ZERO_INTEGER = 'POSITIVE_OR_ZERO_INTEGER',
    COORDINATE = 'COORDINATE',
    URL = 'URL',
    FILE = 'FILE',
    IMAGE = 'IMAGE'
}

export interface IAttribute {
    id?: number;
    uid?: string;
    code?: string;
    name?: string;
    valueType?: ValueType;
    isMandatory?: boolean;
    isUnique?: boolean;
    sortOrder?: number;
    isUserAttribute?: boolean;
    optionSet?: IOptionSet;
}

export class Attribute implements IAttribute {
    constructor(
        public id?: number,
        public uid?: string,
        public code?: string,
        public name?: string,
        public valueType?: ValueType,
        public isMandatory?: boolean,
        public isUnique?: boolean,
        public sortOrder?: number,
        public isUserAttribute?: boolean,
        public optionSet?: IOptionSet
    ) {
        this.isMandatory = this.isMandatory || false;
        this.isUnique = this.isUnique || false;
        this.isUserAttribute = this.isUserAttribute || false;
    }
}
