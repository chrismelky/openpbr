import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenpbrSharedModule } from 'app/shared';
import {
    OrganisationUnitComponent,
    OrganisationUnitDetailComponent,
    OrganisationUnitUpdateComponent,
    OrganisationUnitDeletePopupComponent,
    OrganisationUnitDeleteDialogComponent,
    organisationUnitRoute,
    organisationUnitPopupRoute
} from './';

const ENTITY_STATES = [...organisationUnitRoute, ...organisationUnitPopupRoute];

@NgModule({
    imports: [OpenpbrSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrganisationUnitComponent,
        OrganisationUnitDetailComponent,
        OrganisationUnitUpdateComponent,
        OrganisationUnitDeleteDialogComponent,
        OrganisationUnitDeletePopupComponent
    ],
    entryComponents: [
        OrganisationUnitComponent,
        OrganisationUnitUpdateComponent,
        OrganisationUnitDeleteDialogComponent,
        OrganisationUnitDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrOrganisationUnitModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
