/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitUpdateComponent } from 'app/entities/planning-unit/planning-unit-update.component';
import { PlanningUnitService } from 'app/entities/planning-unit/planning-unit.service';
import { PlanningUnit } from 'app/shared/model/planning-unit.model';

describe('Component Tests', () => {
    describe('PlanningUnit Management Update Component', () => {
        let comp: PlanningUnitUpdateComponent;
        let fixture: ComponentFixture<PlanningUnitUpdateComponent>;
        let service: PlanningUnitService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitUpdateComponent]
            })
                .overrideTemplate(PlanningUnitUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PlanningUnitUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanningUnitService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new PlanningUnit(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.planningUnit = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new PlanningUnit();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.planningUnit = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
