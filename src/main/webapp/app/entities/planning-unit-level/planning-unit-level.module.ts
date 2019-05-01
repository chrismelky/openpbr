import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenpbrSharedModule } from 'app/shared';
import {
    PlanningUnitLevelComponent,
    PlanningUnitLevelDetailComponent,
    PlanningUnitLevelUpdateComponent,
    PlanningUnitLevelDeletePopupComponent,
    PlanningUnitLevelDeleteDialogComponent,
    planningUnitLevelRoute,
    planningUnitLevelPopupRoute
} from './';

const ENTITY_STATES = [...planningUnitLevelRoute, ...planningUnitLevelPopupRoute];

@NgModule({
    imports: [OpenpbrSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PlanningUnitLevelComponent,
        PlanningUnitLevelDetailComponent,
        PlanningUnitLevelUpdateComponent,
        PlanningUnitLevelDeleteDialogComponent,
        PlanningUnitLevelDeletePopupComponent
    ],
    entryComponents: [
        PlanningUnitLevelComponent,
        PlanningUnitLevelUpdateComponent,
        PlanningUnitLevelDeleteDialogComponent,
        PlanningUnitLevelDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrPlanningUnitLevelModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
