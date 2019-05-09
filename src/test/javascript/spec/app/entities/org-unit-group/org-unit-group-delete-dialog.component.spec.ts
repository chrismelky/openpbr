/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OpenpbrTestModule } from '../../../test.module';
import { OrgUnitGroupDeleteDialogComponent } from 'app/entities/org-unit-group/org-unit-group-delete-dialog.component';
import { OrgUnitGroupService } from 'app/entities/org-unit-group/org-unit-group.service';

describe('Component Tests', () => {
    describe('OrgUnitGroup Management Delete Component', () => {
        let comp: OrgUnitGroupDeleteDialogComponent;
        let fixture: ComponentFixture<OrgUnitGroupDeleteDialogComponent>;
        let service: OrgUnitGroupService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrgUnitGroupDeleteDialogComponent]
            })
                .overrideTemplate(OrgUnitGroupDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrgUnitGroupDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrgUnitGroupService);
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
