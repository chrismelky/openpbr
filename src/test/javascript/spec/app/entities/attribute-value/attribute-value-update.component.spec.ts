/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { AttributeValueUpdateComponent } from 'app/entities/attribute-value/attribute-value-update.component';
import { AttributeValueService } from 'app/entities/attribute-value/attribute-value.service';
import { AttributeValue } from 'app/shared/model/attribute-value.model';

describe('Component Tests', () => {
    describe('AttributeValue Management Update Component', () => {
        let comp: AttributeValueUpdateComponent;
        let fixture: ComponentFixture<AttributeValueUpdateComponent>;
        let service: AttributeValueService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [AttributeValueUpdateComponent]
            })
                .overrideTemplate(AttributeValueUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AttributeValueUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttributeValueService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new AttributeValue(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.attributeValue = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new AttributeValue();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.attributeValue = entity;
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
