import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OpenpbrSharedModule } from 'app/shared';
import {
    UserInfoComponent,
    UserInfoDetailComponent,
    UserInfoUpdateComponent,
    UserInfoDeletePopupComponent,
    UserInfoDeleteDialogComponent,
    userInfoRoute,
    userInfoPopupRoute
} from './';

const ENTITY_STATES = [...userInfoRoute, ...userInfoPopupRoute];

@NgModule({
    imports: [OpenpbrSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserInfoComponent,
        UserInfoDetailComponent,
        UserInfoUpdateComponent,
        UserInfoDeleteDialogComponent,
        UserInfoDeletePopupComponent
    ],
    entryComponents: [UserInfoComponent, UserInfoUpdateComponent, UserInfoDeleteDialogComponent, UserInfoDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrUserInfoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
