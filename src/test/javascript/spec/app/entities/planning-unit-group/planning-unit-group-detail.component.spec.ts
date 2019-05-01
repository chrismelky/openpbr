/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitGroupDetailComponent } from 'app/entities/planning-unit-group/planning-unit-group-detail.component';
import { PlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';

describe('Component Tests', () => {
    describe('PlanningUnitGroup Management Detail Component', () => {
        let comp: PlanningUnitGroupDetailComponent;
        let fixture: ComponentFixture<PlanningUnitGroupDetailComponent>;
        const route = ({ data: of({ planningUnitGroup: new PlanningUnitGroup(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitGroupDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PlanningUnitGroupDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanningUnitGroupDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.planningUnitGroup).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
