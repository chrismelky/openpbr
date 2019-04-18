/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { OrganisationUnitUpdateComponent } from 'app/entities/organisation-unit/organisation-unit-update.component';
import { OrganisationUnitService } from 'app/entities/organisation-unit/organisation-unit.service';
import { OrganisationUnit } from 'app/shared/model/organisation-unit.model';

describe('Component Tests', () => {
    describe('OrganisationUnit Management Update Component', () => {
        let comp: OrganisationUnitUpdateComponent;
        let fixture: ComponentFixture<OrganisationUnitUpdateComponent>;
        let service: OrganisationUnitService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrganisationUnitUpdateComponent]
            })
                .overrideTemplate(OrganisationUnitUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrganisationUnitUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrganisationUnitService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new OrganisationUnit(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.organisationUnit = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new OrganisationUnit();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.organisationUnit = entity;
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
