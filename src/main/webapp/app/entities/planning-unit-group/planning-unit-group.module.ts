import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenpbrSharedModule } from 'app/shared';
import {
    PlanningUnitGroupComponent,
    PlanningUnitGroupDetailComponent,
    PlanningUnitGroupUpdateComponent,
    PlanningUnitGroupDeletePopupComponent,
    PlanningUnitGroupDeleteDialogComponent,
    planningUnitGroupRoute,
    planningUnitGroupPopupRoute
} from './';

const ENTITY_STATES = [...planningUnitGroupRoute, ...planningUnitGroupPopupRoute];

@NgModule({
    imports: [OpenpbrSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PlanningUnitGroupComponent,
        PlanningUnitGroupDetailComponent,
        PlanningUnitGroupUpdateComponent,
        PlanningUnitGroupDeleteDialogComponent,
        PlanningUnitGroupDeletePopupComponent
    ],
    entryComponents: [
        PlanningUnitGroupComponent,
        PlanningUnitGroupUpdateComponent,
        PlanningUnitGroupDeleteDialogComponent,
        PlanningUnitGroupDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrPlanningUnitGroupModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
