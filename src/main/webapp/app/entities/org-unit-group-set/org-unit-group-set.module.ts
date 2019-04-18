import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenpbrSharedModule } from 'app/shared';
import {
    OrgUnitGroupSetComponent,
    OrgUnitGroupSetDetailComponent,
    OrgUnitGroupSetUpdateComponent,
    OrgUnitGroupSetDeletePopupComponent,
    OrgUnitGroupSetDeleteDialogComponent,
    orgUnitGroupSetRoute,
    orgUnitGroupSetPopupRoute
} from './';

const ENTITY_STATES = [...orgUnitGroupSetRoute, ...orgUnitGroupSetPopupRoute];

@NgModule({
    imports: [OpenpbrSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrgUnitGroupSetComponent,
        OrgUnitGroupSetDetailComponent,
        OrgUnitGroupSetUpdateComponent,
        OrgUnitGroupSetDeleteDialogComponent,
        OrgUnitGroupSetDeletePopupComponent
    ],
    entryComponents: [
        OrgUnitGroupSetComponent,
        OrgUnitGroupSetUpdateComponent,
        OrgUnitGroupSetDeleteDialogComponent,
        OrgUnitGroupSetDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrOrgUnitGroupSetModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
