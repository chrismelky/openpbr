import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { OpenpbrSharedLibsModule, OpenpbrSharedCommonModule, PbrLoginModalComponent, HasAnyAuthorityDirective } from './';

import { JhMaterialModule } from 'app/shared/jh-material.module';
@NgModule({
    imports: [JhMaterialModule, OpenpbrSharedLibsModule, OpenpbrSharedCommonModule],
    declarations: [PbrLoginModalComponent, HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [PbrLoginModalComponent],
    exports: [JhMaterialModule, OpenpbrSharedCommonModule, PbrLoginModalComponent, HasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrSharedModule {
    static forRoot() {
        return {
            ngModule: OpenpbrSharedModule
        };
    }
}
