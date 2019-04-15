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
                path: 'option-set',
                loadChildren: './option-set/option-set.module#OpenpbrOptionSetModule'
            },
            {
                path: 'option-value',
                loadChildren: './option-value/option-value.module#OpenpbrOptionValueModule'
            },
            {
                path: 'attribute',
                loadChildren: './attribute/attribute.module#OpenpbrAttributeModule'
            },
            {
                path: 'attribute-value',
                loadChildren: './attribute-value/attribute-value.module#OpenpbrAttributeValueModule'
            },
            {
                path: 'attribute',
                loadChildren: './attribute/attribute.module#OpenpbrAttributeModule'
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
