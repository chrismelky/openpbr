/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OpenpbrTestModule } from '../../../test.module';
import { OrgUnitGroupSetDeleteDialogComponent } from 'app/entities/org-unit-group-set/org-unit-group-set-delete-dialog.component';
import { OrgUnitGroupSetService } from 'app/entities/org-unit-group-set/org-unit-group-set.service';

describe('Component Tests', () => {
    describe('OrgUnitGroupSet Management Delete Component', () => {
        let comp: OrgUnitGroupSetDeleteDialogComponent;
        let fixture: ComponentFixture<OrgUnitGroupSetDeleteDialogComponent>;
        let service: OrgUnitGroupSetService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrgUnitGroupSetDeleteDialogComponent]
            })
                .overrideTemplate(OrgUnitGroupSetDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrgUnitGroupSetDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrgUnitGroupSetService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
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
