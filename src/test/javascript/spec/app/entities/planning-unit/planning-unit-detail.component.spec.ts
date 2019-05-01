/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitDetailComponent } from 'app/entities/planning-unit/planning-unit-detail.component';
import { PlanningUnit } from 'app/shared/model/planning-unit.model';

describe('Component Tests', () => {
    describe('PlanningUnit Management Detail Component', () => {
        let comp: PlanningUnitDetailComponent;
        let fixture: ComponentFixture<PlanningUnitDetailComponent>;
        const route = ({ data: of({ planningUnit: new PlanningUnit(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PlanningUnitDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanningUnitDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.planningUnit).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
