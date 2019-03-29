import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'user-info',
                loadChildren: './user-info/user-info.module#OpenpbrUserInfoModule'
            },
            {
                path: 'user-info',
                loadChildren: './user-info/user-info.module#OpenpbrUserInfoModule'
            },
            {
                path: 'user-info',
                loadChildren: './user-info/user-info.module#OpenpbrUserInfoModule'
            },
            {
                path: 'user-info',
                loadChildren: './user-info/user-info.module#OpenpbrUserInfoModule'
            },
            {
                path: 'user-info',
                loadChildren: './user-info/user-info.module#OpenpbrUserInfoModule'
            },
            {
                path: 'user-info',
                loadChildren: './user-info/user-info.module#OpenpbrUserInfoModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenpbrEntityModule {}
