/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { OptionSetUpdateComponent } from 'app/entities/option-set/option-set-update.component';
import { OptionSetService } from 'app/entities/option-set/option-set.service';
import { OptionSet } from 'app/shared/model/option-set.model';

describe('Component Tests', () => {
    describe('OptionSet Management Update Component', () => {
        let comp: OptionSetUpdateComponent;
        let fixture: ComponentFixture<OptionSetUpdateComponent>;
        let service: OptionSetService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OptionSetUpdateComponent]
            })
                .overrideTemplate(OptionSetUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OptionSetUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OptionSetService);
        });

        describe('save', () => {
            it('Should call update treeNodeService on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new OptionSet(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.optionSet = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create treeNodeService on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new OptionSet();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.optionSet = entity;
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
