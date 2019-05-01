/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitLevelDetailComponent } from 'app/entities/planning-unit-level/planning-unit-level-detail.component';
import { PlanningUnitLevel } from 'app/shared/model/planning-unit-level.model';

describe('Component Tests', () => {
    describe('PlanningUnitLevel Management Detail Component', () => {
        let comp: PlanningUnitLevelDetailComponent;
        let fixture: ComponentFixture<PlanningUnitLevelDetailComponent>;
        const route = ({ data: of({ planningUnitLevel: new PlanningUnitLevel(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitLevelDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PlanningUnitLevelDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanningUnitLevelDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.planningUnitLevel).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
