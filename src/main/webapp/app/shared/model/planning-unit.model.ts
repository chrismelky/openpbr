import { IPlanningUnit } from 'app/shared/model/planning-unit.model';
import { IPlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';

export interface IPlanningUnit {
    id?: number;
    uid?: string;
    code?: string;
    name?: string;
    level?: number;
    sortOrder?: number;
    isActive?: boolean;
    parent?: IPlanningUnit;
    planningUnitGroups?: IPlanningUnitGroup[];
}

export class PlanningUnit implements IPlanningUnit {
    constructor(
        public id?: number,
        public uid?: string,
        public code?: string,
        public name?: string,
        public level?: number,
        public sortOrder?: number,
        public isActive?: boolean,
        public parent?: IPlanningUnit,
        public planningUnitGroups?: IPlanningUnitGroup[]
    ) {
        this.isActive = this.isActive || false;
    }
}
