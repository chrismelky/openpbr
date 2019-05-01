import { IPlanningUnit } from 'app/shared/model/planning-unit.model';
import { IPlanningUnitGroupSet } from 'app/shared/model/planning-unit-group-set.model';

export interface IPlanningUnitGroup {
    id?: number;
    uid?: string;
    code?: string;
    name?: string;
    sortOrder?: number;
    isActive?: boolean;
    planningUnits?: IPlanningUnit[];
    planningUnitGroupSets?: IPlanningUnitGroupSet[];
}

export class PlanningUnitGroup implements IPlanningUnitGroup {
    constructor(
        public id?: number,
        public uid?: string,
        public code?: string,
        public name?: string,
        public sortOrder?: number,
        public isActive?: boolean,
        public planningUnits?: IPlanningUnit[],
        public planningUnitGroupSets?: IPlanningUnitGroupSet[]
    ) {
        this.isActive = this.isActive || false;
    }
}
