import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenpbrSharedModule } from 'app/shared';
import {
    OptionSetComponent,
    OptionSetDetailComponent,
    OptionSetUpdateComponent,
    OptionSetDeletePopupComponent,
    OptionSetDeleteDialogComponent,
    optionSetRoute,
    optionSetPopupRoute
} from './';

const ENTITY_STATES = [...optionSetRoute, ...optionSetPopupRoute];

@NgModule({
    imports: [OpenpbrSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OptionSetComponent,
        OptionSetDetailComponent,
        OptionSetUpdateComponent,
        OptionSetDeleteDialogComponent,
        OptionSetDeletePopupComponent
    ],
    entryComponents: [OptionSetComponent, OptionSetUpdateComponent, OptionSetDeleteDialogComponent, OptionSetDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrOptionSetModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
