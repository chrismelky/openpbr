/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitLevelUpdateComponent } from 'app/entities/planning-unit-level/planning-unit-level-update.component';
import { PlanningUnitLevelService } from 'app/entities/planning-unit-level/planning-unit-level.service';
import { PlanningUnitLevel } from 'app/shared/model/planning-unit-level.model';

describe('Component Tests', () => {
    describe('PlanningUnitLevel Management Update Component', () => {
        let comp: PlanningUnitLevelUpdateComponent;
        let fixture: ComponentFixture<PlanningUnitLevelUpdateComponent>;
        let service: PlanningUnitLevelService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitLevelUpdateComponent]
            })
                .overrideTemplate(PlanningUnitLevelUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PlanningUnitLevelUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanningUnitLevelService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new PlanningUnitLevel(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.planningUnitLevel = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new PlanningUnitLevel();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.planningUnitLevel = entity;
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
