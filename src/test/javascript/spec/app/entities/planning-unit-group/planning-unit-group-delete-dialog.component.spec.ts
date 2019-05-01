/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitGroupDeleteDialogComponent } from 'app/entities/planning-unit-group/planning-unit-group-delete-dialog.component';
import { PlanningUnitGroupService } from 'app/entities/planning-unit-group/planning-unit-group.service';

describe('Component Tests', () => {
    describe('PlanningUnitGroup Management Delete Component', () => {
        let comp: PlanningUnitGroupDeleteDialogComponent;
        let fixture: ComponentFixture<PlanningUnitGroupDeleteDialogComponent>;
        let service: PlanningUnitGroupService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitGroupDeleteDialogComponent]
            })
                .overrideTemplate(PlanningUnitGroupDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanningUnitGroupDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanningUnitGroupService);
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
