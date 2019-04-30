/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OpenpbrTestModule } from '../../../test.module';
import { AttributeValueDeleteDialogComponent } from 'app/entities/attribute-value/attribute-value-delete-dialog.component';
import { AttributeValueService } from 'app/entities/attribute-value/attribute-value.service';

describe('Component Tests', () => {
    describe('AttributeValue Management Delete Component', () => {
        let comp: AttributeValueDeleteDialogComponent;
        let fixture: ComponentFixture<AttributeValueDeleteDialogComponent>;
        let service: AttributeValueService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [AttributeValueDeleteDialogComponent]
            })
                .overrideTemplate(AttributeValueDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AttributeValueDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttributeValueService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete treeNodeService on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
