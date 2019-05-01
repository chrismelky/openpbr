export interface IPlanningUnitLevel {
    id?: number;
    uid?: string;
    code?: string;
    name?: string;
    level?: number;
    isActive?: boolean;
}

export class PlanningUnitLevel implements IPlanningUnitLevel {
    constructor(
        public id?: number,
        public uid?: string,
        public code?: string,
        public name?: string,
        public level?: number,
        public isActive?: boolean
    ) {
        this.isActive = this.isActive || false;
    }
}
