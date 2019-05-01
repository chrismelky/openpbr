/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitGroupSetUpdateComponent } from 'app/entities/planning-unit-group-set/planning-unit-group-set-update.component';
import { PlanningUnitGroupSetService } from 'app/entities/planning-unit-group-set/planning-unit-group-set.service';
import { PlanningUnitGroupSet } from 'app/shared/model/planning-unit-group-set.model';

describe('Component Tests', () => {
    describe('PlanningUnitGroupSet Management Update Component', () => {
        let comp: PlanningUnitGroupSetUpdateComponent;
        let fixture: ComponentFixture<PlanningUnitGroupSetUpdateComponent>;
        let service: PlanningUnitGroupSetService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitGroupSetUpdateComponent]
            })
                .overrideTemplate(PlanningUnitGroupSetUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PlanningUnitGroupSetUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanningUnitGroupSetService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new PlanningUnitGroupSet(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.planningUnitGroupSet = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new PlanningUnitGroupSet();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.planningUnitGroupSet = entity;
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
