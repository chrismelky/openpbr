import { IPlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';

export interface IPlanningUnitGroupSet {
    id?: number;
    uid?: string;
    code?: string;
    name?: string;
    sortOrder?: number;
    isActive?: boolean;
    planningUnitGroups?: IPlanningUnitGroup[];
}

export class PlanningUnitGroupSet implements IPlanningUnitGroupSet {
    constructor(
        public id?: number,
        public uid?: string,
        public code?: string,
        public name?: string,
        public sortOrder?: number,
        public isActive?: boolean,
        public planningUnitGroups?: IPlanningUnitGroup[]
    ) {
        this.isActive = this.isActive || false;
    }
}
