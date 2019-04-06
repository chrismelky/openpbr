import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenpbrSharedModule } from 'app/shared';
import {
    AttributeValueComponent,
    AttributeValueDetailComponent,
    AttributeValueUpdateComponent,
    AttributeValueDeletePopupComponent,
    AttributeValueDeleteDialogComponent,
    attributeValueRoute,
    attributeValuePopupRoute
} from './';

const ENTITY_STATES = [...attributeValueRoute, ...attributeValuePopupRoute];

@NgModule({
    imports: [OpenpbrSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AttributeValueComponent,
        AttributeValueDetailComponent,
        AttributeValueUpdateComponent,
        AttributeValueDeleteDialogComponent,
        AttributeValueDeletePopupComponent
    ],
    entryComponents: [
        AttributeValueComponent,
        AttributeValueUpdateComponent,
        AttributeValueDeleteDialogComponent,
        AttributeValueDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrAttributeValueModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
