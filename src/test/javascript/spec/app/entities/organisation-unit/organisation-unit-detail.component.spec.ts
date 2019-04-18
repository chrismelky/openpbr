/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { OrganisationUnitDetailComponent } from 'app/entities/organisation-unit/organisation-unit-detail.component';
import { OrganisationUnit } from 'app/shared/model/organisation-unit.model';

describe('Component Tests', () => {
    describe('OrganisationUnit Management Detail Component', () => {
        let comp: OrganisationUnitDetailComponent;
        let fixture: ComponentFixture<OrganisationUnitDetailComponent>;
        const route = ({ data: of({ organisationUnit: new OrganisationUnit(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrganisationUnitDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OrganisationUnitDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrganisationUnitDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.organisationUnit).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
