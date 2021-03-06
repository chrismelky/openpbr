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
            },
            {
                path: 'user-info',
                loadChildren: './user-info/user-info.module#OpenpbrUserInfoModule'
            },
            {
                path: 'org-unit-level',
                loadChildren: './org-unit-level/org-unit-level.module#OpenpbrOrgUnitLevelModule'
            },
            {
                path: 'org-unit-group-set',
                loadChildren: './org-unit-group-set/org-unit-group-set.module#OpenpbrOrgUnitGroupSetModule'
            },
            {
                path: 'org-unit-group',
                loadChildren: './org-unit-group/org-unit-group.module#OpenpbrOrgUnitGroupModule'
            },
            {
                path: 'organisation-unit',
                loadChildren: './organisation-unit/organisation-unit.module#OpenpbrOrganisationUnitModule'
            },
            {
                path: 'planning-unit-group-set',
                loadChildren: './planning-unit-group-set/planning-unit-group-set.module#OpenpbrPlanningUnitGroupSetModule'
            },
            {
                path: 'planning-unit-group',
                loadChildren: './planning-unit-group/planning-unit-group.module#OpenpbrPlanningUnitGroupModule'
            },
            {
                path: 'planning-unit-level',
                loadChildren: './planning-unit-level/planning-unit-level.module#OpenpbrPlanningUnitLevelModule'
            },
            {
                path: 'planning-unit',
                loadChildren: './planning-unit/planning-unit.module#OpenpbrPlanningUnitModule'
            },
            {
                path: 'org-unit-group-set',
                loadChildren: './org-unit-group-set/org-unit-group-set.module#OpenpbrOrgUnitGroupSetModule'
            },
            {
                path: 'org-unit-group',
                loadChildren: './org-unit-group/org-unit-group.module#OpenpbrOrgUnitGroupModule'
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
