/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { OrgUnitLevelUpdateComponent } from 'app/entities/org-unit-level/org-unit-level-update.component';
import { OrgUnitLevelService } from 'app/entities/org-unit-level/org-unit-level.service';
import { OrgUnitLevel } from 'app/shared/model/org-unit-level.model';

describe('Component Tests', () => {
    describe('OrgUnitLevel Management Update Component', () => {
        let comp: OrgUnitLevelUpdateComponent;
        let fixture: ComponentFixture<OrgUnitLevelUpdateComponent>;
        let service: OrgUnitLevelService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrgUnitLevelUpdateComponent]
            })
                .overrideTemplate(OrgUnitLevelUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrgUnitLevelUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrgUnitLevelService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new OrgUnitLevel(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.orgUnitLevel = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new OrgUnitLevel();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.orgUnitLevel = entity;
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
