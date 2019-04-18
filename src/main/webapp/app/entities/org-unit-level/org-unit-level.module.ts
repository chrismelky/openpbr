import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenpbrSharedModule } from 'app/shared';
import {
    OrgUnitLevelComponent,
    OrgUnitLevelDetailComponent,
    OrgUnitLevelUpdateComponent,
    OrgUnitLevelDeletePopupComponent,
    OrgUnitLevelDeleteDialogComponent,
    orgUnitLevelRoute,
    orgUnitLevelPopupRoute
} from './';

const ENTITY_STATES = [...orgUnitLevelRoute, ...orgUnitLevelPopupRoute];

@NgModule({
    imports: [OpenpbrSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrgUnitLevelComponent,
        OrgUnitLevelDetailComponent,
        OrgUnitLevelUpdateComponent,
        OrgUnitLevelDeleteDialogComponent,
        OrgUnitLevelDeletePopupComponent
    ],
    entryComponents: [
        OrgUnitLevelComponent,
        OrgUnitLevelUpdateComponent,
        OrgUnitLevelDeleteDialogComponent,
        OrgUnitLevelDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrOrgUnitLevelModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
