/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitGroupSetDetailComponent } from 'app/entities/planning-unit-group-set/planning-unit-group-set-detail.component';
import { PlanningUnitGroupSet } from 'app/shared/model/planning-unit-group-set.model';

describe('Component Tests', () => {
    describe('PlanningUnitGroupSet Management Detail Component', () => {
        let comp: PlanningUnitGroupSetDetailComponent;
        let fixture: ComponentFixture<PlanningUnitGroupSetDetailComponent>;
        const route = ({ data: of({ planningUnitGroupSet: new PlanningUnitGroupSet(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitGroupSetDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PlanningUnitGroupSetDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanningUnitGroupSetDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.planningUnitGroupSet).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
