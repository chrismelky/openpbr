/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { OrgUnitGroupDetailComponent } from 'app/entities/org-unit-group/org-unit-group-detail.component';
import { OrgUnitGroup } from 'app/shared/model/org-unit-group.model';

describe('Component Tests', () => {
    describe('OrgUnitGroup Management Detail Component', () => {
        let comp: OrgUnitGroupDetailComponent;
        let fixture: ComponentFixture<OrgUnitGroupDetailComponent>;
        const route = ({ data: of({ orgUnitGroup: new OrgUnitGroup(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrgUnitGroupDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OrgUnitGroupDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrgUnitGroupDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.orgUnitGroup).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
