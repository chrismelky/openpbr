/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { OptionValueUpdateComponent } from 'app/entities/option-value/option-value-update.component';
import { OptionValueService } from 'app/entities/option-value/option-value.service';
import { OptionValue } from 'app/shared/model/option-value.model';

describe('Component Tests', () => {
    describe('OptionValue Management Update Component', () => {
        let comp: OptionValueUpdateComponent;
        let fixture: ComponentFixture<OptionValueUpdateComponent>;
        let service: OptionValueService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OptionValueUpdateComponent]
            })
                .overrideTemplate(OptionValueUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OptionValueUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OptionValueService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new OptionValue(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.optionValue = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new OptionValue();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.optionValue = entity;
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
