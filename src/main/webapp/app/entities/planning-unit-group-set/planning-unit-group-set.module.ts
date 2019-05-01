import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenpbrSharedModule } from 'app/shared';
import {
    PlanningUnitGroupSetComponent,
    PlanningUnitGroupSetDetailComponent,
    PlanningUnitGroupSetUpdateComponent,
    PlanningUnitGroupSetDeletePopupComponent,
    PlanningUnitGroupSetDeleteDialogComponent,
    planningUnitGroupSetRoute,
    planningUnitGroupSetPopupRoute
} from './';

const ENTITY_STATES = [...planningUnitGroupSetRoute, ...planningUnitGroupSetPopupRoute];

@NgModule({
    imports: [OpenpbrSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PlanningUnitGroupSetComponent,
        PlanningUnitGroupSetDetailComponent,
        PlanningUnitGroupSetUpdateComponent,
        PlanningUnitGroupSetDeleteDialogComponent,
        PlanningUnitGroupSetDeletePopupComponent
    ],
    entryComponents: [
        PlanningUnitGroupSetComponent,
        PlanningUnitGroupSetUpdateComponent,
        PlanningUnitGroupSetDeleteDialogComponent,
        PlanningUnitGroupSetDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrPlanningUnitGroupSetModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
