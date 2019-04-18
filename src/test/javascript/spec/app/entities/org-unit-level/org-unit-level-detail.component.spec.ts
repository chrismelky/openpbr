/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { OrgUnitLevelDetailComponent } from 'app/entities/org-unit-level/org-unit-level-detail.component';
import { OrgUnitLevel } from 'app/shared/model/org-unit-level.model';

describe('Component Tests', () => {
    describe('OrgUnitLevel Management Detail Component', () => {
        let comp: OrgUnitLevelDetailComponent;
        let fixture: ComponentFixture<OrgUnitLevelDetailComponent>;
        const route = ({ data: of({ orgUnitLevel: new OrgUnitLevel(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrgUnitLevelDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OrgUnitLevelDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrgUnitLevelDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.orgUnitLevel).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
