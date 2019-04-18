/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { OrgUnitGroupSetDetailComponent } from 'app/entities/org-unit-group-set/org-unit-group-set-detail.component';
import { OrgUnitGroupSet } from 'app/shared/model/org-unit-group-set.model';

describe('Component Tests', () => {
    describe('OrgUnitGroupSet Management Detail Component', () => {
        let comp: OrgUnitGroupSetDetailComponent;
        let fixture: ComponentFixture<OrgUnitGroupSetDetailComponent>;
        const route = ({ data: of({ orgUnitGroupSet: new OrgUnitGroupSet(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrgUnitGroupSetDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OrgUnitGroupSetDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrgUnitGroupSetDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.orgUnitGroupSet).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
