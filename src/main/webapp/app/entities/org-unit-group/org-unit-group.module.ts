import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenpbrSharedModule } from 'app/shared';
import {
    OrgUnitGroupComponent,
    OrgUnitGroupDetailComponent,
    OrgUnitGroupUpdateComponent,
    OrgUnitGroupDeletePopupComponent,
    OrgUnitGroupDeleteDialogComponent,
    orgUnitGroupRoute,
    orgUnitGroupPopupRoute
} from './';

const ENTITY_STATES = [...orgUnitGroupRoute, ...orgUnitGroupPopupRoute];

@NgModule({
    imports: [OpenpbrSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrgUnitGroupComponent,
        OrgUnitGroupDetailComponent,
        OrgUnitGroupUpdateComponent,
        OrgUnitGroupDeleteDialogComponent,
        OrgUnitGroupDeletePopupComponent
    ],
    entryComponents: [
        OrgUnitGroupComponent,
        OrgUnitGroupUpdateComponent,
        OrgUnitGroupDeleteDialogComponent,
        OrgUnitGroupDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrOrgUnitGroupModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
