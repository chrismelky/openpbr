import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { OpenpbrSharedModule } from 'app/shared';
import { EntityAuditModule } from './entity-audit/entity-audit.module';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

import {
    adminState,
    AuditsComponent,
    UserMgmtComponent,
    UserMgmtDetailComponent,
    UserMgmtUpdateComponent,
    UserMgmtDeleteDialogComponent,
    LogsComponent,
    PbrMetricsMonitoringComponent,
    PbrHealthModalComponent,
    PbrHealthCheckComponent,
    PbrConfigurationComponent,
    PbrDocsComponent,
    PbrTrackerComponent
} from './';

@NgModule({
    imports: [
        OpenpbrSharedModule,
        RouterModule.forChild(adminState),
        EntityAuditModule
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    declarations: [
        AuditsComponent,
        UserMgmtComponent,
        UserMgmtDetailComponent,
        UserMgmtUpdateComponent,
        UserMgmtDeleteDialogComponent,
        LogsComponent,
        PbrConfigurationComponent,
        PbrHealthCheckComponent,
        PbrHealthModalComponent,
        PbrDocsComponent,
        PbrTrackerComponent,
        PbrMetricsMonitoringComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    entryComponents: [UserMgmtDeleteDialogComponent, PbrHealthModalComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrAdminModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
