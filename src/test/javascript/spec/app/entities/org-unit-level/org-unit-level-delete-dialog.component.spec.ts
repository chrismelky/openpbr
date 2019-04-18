/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OpenpbrTestModule } from '../../../test.module';
import { OrgUnitLevelDeleteDialogComponent } from 'app/entities/org-unit-level/org-unit-level-delete-dialog.component';
import { OrgUnitLevelService } from 'app/entities/org-unit-level/org-unit-level.service';

describe('Component Tests', () => {
    describe('OrgUnitLevel Management Delete Component', () => {
        let comp: OrgUnitLevelDeleteDialogComponent;
        let fixture: ComponentFixture<OrgUnitLevelDeleteDialogComponent>;
        let service: OrgUnitLevelService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrgUnitLevelDeleteDialogComponent]
            })
                .overrideTemplate(OrgUnitLevelDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrgUnitLevelDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrgUnitLevelService);
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
