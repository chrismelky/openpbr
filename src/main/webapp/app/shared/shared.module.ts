import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { OpenpbrSharedLibsModule, OpenpbrSharedCommonModule, PbrLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
    imports: [OpenpbrSharedLibsModule, OpenpbrSharedCommonModule],
    declarations: [PbrLoginModalComponent, HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [PbrLoginModalComponent],
    exports: [OpenpbrSharedCommonModule, PbrLoginModalComponent, HasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrSharedModule {
    static forRoot() {
        return {
            ngModule: OpenpbrSharedModule
        };
    }
}
