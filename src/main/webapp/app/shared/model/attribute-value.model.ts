import { IAttribute } from 'app/shared/model/attribute.model';

export interface IAttributeValue {
    id?: number;
    value?: string;
    attribute?: IAttribute;
}

export class AttributeValue implements IAttributeValue {
    constructor(public id?: number, public value?: string, public attribute?: IAttribute) {}
}
