export interface IOrgUnitLevel {
    id?: number;
    uid?: string;
    code?: string;
    name?: string;
    level?: number;
}

export class OrgUnitLevel implements IOrgUnitLevel {
    constructor(public id?: number, public uid?: string, public code?: string, public name?: string, public level?: number) {}
}
