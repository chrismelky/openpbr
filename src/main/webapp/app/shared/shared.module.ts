import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { OpenpbrSharedLibsModule, OpenpbrSharedCommonModule, PbrLoginModalComponent, HasAnyAuthorityDirective } from './';
import { AddButtonComponent } from './button/add-button.component';

import { JhMaterialModule } from 'app/shared/jh-material.module';
import { MultselectComponent } from './multselect/multselect.component';
import { ActionMenuComponent } from './action-menu/action-menu.component';
import { TreeViewComponent } from './treeview/tree-view.component';
import { AttributesComponent } from './attributes/attributes.component';
import { OrgUnitFilterComponent } from './org-unit-filter/org-unit-filter.component';
import { FlexLayoutModule } from '@angular/flex-layout';

@NgModule({
    imports: [JhMaterialModule, OpenpbrSharedLibsModule, OpenpbrSharedCommonModule, RouterModule, FlexLayoutModule],
    declarations: [
        PbrLoginModalComponent,
        HasAnyAuthorityDirective,
        AddButtonComponent,
        MultselectComponent,
        ActionMenuComponent,
        TreeViewComponent,
        AttributesComponent,
        OrgUnitFilterComponent
    ],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [PbrLoginModalComponent],
    exports: [
        JhMaterialModule,
        OpenpbrSharedCommonModule,
        PbrLoginModalComponent,
        HasAnyAuthorityDirective,
        AddButtonComponent,
        MultselectComponent,
        ActionMenuComponent,
        TreeViewComponent,
        AttributesComponent,
        OrgUnitFilterComponent,
        FlexLayoutModule
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
