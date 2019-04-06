import { IOptionValue } from 'app/shared/model/option-value.model';

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

export interface IOptionSet {
    id?: number;
    uid?: string;
    code?: string;
    name?: string;
    valueType?: ValueType;
    sortOrder?: number;
    optionValues?: IOptionValue[];
}

export class OptionSet implements IOptionSet {
    constructor(
        public id?: number,
        public uid?: string,
        public code?: string,
        public name?: string,
        public valueType?: ValueType,
        public sortOrder?: number,
        public optionValues?: IOptionValue[]
    ) {}
}
