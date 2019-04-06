import { IOptionSet } from 'app/shared/model/option-set.model';

export interface IOptionValue {
    id?: number;
    uid?: string;
    code?: string;
    name?: string;
    sortOrder?: number;
    optionSet?: IOptionSet;
}

export class OptionValue implements IOptionValue {
    constructor(
        public id?: number,
        public uid?: string,
        public code?: string,
        public name?: string,
        public sortOrder?: number,
        public optionSet?: IOptionSet
    ) {}
}
