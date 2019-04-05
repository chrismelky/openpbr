import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { OpenpbrSharedLibsModule, OpenpbrSharedCommonModule, PbrLoginModalComponent, HasAnyAuthorityDirective } from './';
import { AddButtonComponent } from './button/add-button.component';

import { JhMaterialModule } from 'app/shared/jh-material.module';
import { MultselectComponent } from './multselect/multselect.component';
@NgModule({
    imports: [JhMaterialModule, OpenpbrSharedLibsModule, OpenpbrSharedCommonModule],
    declarations: [PbrLoginModalComponent, HasAnyAuthorityDirective, AddButtonComponent, MultselectComponent],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [PbrLoginModalComponent],
    exports: [
        JhMaterialModule,
        OpenpbrSharedCommonModule,
        PbrLoginModalComponent,
        HasAnyAuthorityDirective,
        AddButtonComponent,
        MultselectComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrSharedModule {
    static forRoot() {
        return {
            ngModule: OpenpbrSharedModule
        };
    }
}
