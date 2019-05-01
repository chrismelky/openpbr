/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitGroupUpdateComponent } from 'app/entities/planning-unit-group/planning-unit-group-update.component';
import { PlanningUnitGroupService } from 'app/entities/planning-unit-group/planning-unit-group.service';
import { PlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';

describe('Component Tests', () => {
    describe('PlanningUnitGroup Management Update Component', () => {
        let comp: PlanningUnitGroupUpdateComponent;
        let fixture: ComponentFixture<PlanningUnitGroupUpdateComponent>;
        let service: PlanningUnitGroupService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitGroupUpdateComponent]
            })
                .overrideTemplate(PlanningUnitGroupUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PlanningUnitGroupUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanningUnitGroupService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new PlanningUnitGroup(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.planningUnitGroup = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new PlanningUnitGroup();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.planningUnitGroup = entity;
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
