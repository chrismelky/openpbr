import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenpbrSharedModule } from 'app/shared';
import {
    PlanningUnitComponent,
    PlanningUnitDetailComponent,
    PlanningUnitUpdateComponent,
    PlanningUnitDeletePopupComponent,
    PlanningUnitDeleteDialogComponent,
    planningUnitRoute,
    planningUnitPopupRoute
} from './';

const ENTITY_STATES = [...planningUnitRoute, ...planningUnitPopupRoute];

@NgModule({
    imports: [OpenpbrSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PlanningUnitComponent,
        PlanningUnitDetailComponent,
        PlanningUnitUpdateComponent,
        PlanningUnitDeleteDialogComponent,
        PlanningUnitDeletePopupComponent
    ],
    entryComponents: [
        PlanningUnitComponent,
        PlanningUnitUpdateComponent,
        PlanningUnitDeleteDialogComponent,
        PlanningUnitDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrPlanningUnitModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
