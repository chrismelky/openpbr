/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { OrgUnitGroupUpdateComponent } from 'app/entities/org-unit-group/org-unit-group-update.component';
import { OrgUnitGroupService } from 'app/entities/org-unit-group/org-unit-group.service';
import { OrgUnitGroup } from 'app/shared/model/org-unit-group.model';

describe('Component Tests', () => {
    describe('OrgUnitGroup Management Update Component', () => {
        let comp: OrgUnitGroupUpdateComponent;
        let fixture: ComponentFixture<OrgUnitGroupUpdateComponent>;
        let service: OrgUnitGroupService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrgUnitGroupUpdateComponent]
            })
                .overrideTemplate(OrgUnitGroupUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrgUnitGroupUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrgUnitGroupService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new OrgUnitGroup(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.orgUnitGroup = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new OrgUnitGroup();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.orgUnitGroup = entity;
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
