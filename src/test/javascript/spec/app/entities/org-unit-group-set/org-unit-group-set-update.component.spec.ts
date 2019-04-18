/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { OrgUnitGroupSetUpdateComponent } from 'app/entities/org-unit-group-set/org-unit-group-set-update.component';
import { OrgUnitGroupSetService } from 'app/entities/org-unit-group-set/org-unit-group-set.service';
import { OrgUnitGroupSet } from 'app/shared/model/org-unit-group-set.model';

describe('Component Tests', () => {
    describe('OrgUnitGroupSet Management Update Component', () => {
        let comp: OrgUnitGroupSetUpdateComponent;
        let fixture: ComponentFixture<OrgUnitGroupSetUpdateComponent>;
        let service: OrgUnitGroupSetService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrgUnitGroupSetUpdateComponent]
            })
                .overrideTemplate(OrgUnitGroupSetUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrgUnitGroupSetUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrgUnitGroupSetService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new OrgUnitGroupSet(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.orgUnitGroupSet = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new OrgUnitGroupSet();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.orgUnitGroupSet = entity;
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
