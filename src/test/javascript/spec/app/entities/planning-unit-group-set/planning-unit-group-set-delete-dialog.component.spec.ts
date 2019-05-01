/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitGroupSetDeleteDialogComponent } from 'app/entities/planning-unit-group-set/planning-unit-group-set-delete-dialog.component';
import { PlanningUnitGroupSetService } from 'app/entities/planning-unit-group-set/planning-unit-group-set.service';

describe('Component Tests', () => {
    describe('PlanningUnitGroupSet Management Delete Component', () => {
        let comp: PlanningUnitGroupSetDeleteDialogComponent;
        let fixture: ComponentFixture<PlanningUnitGroupSetDeleteDialogComponent>;
        let service: PlanningUnitGroupSetService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitGroupSetDeleteDialogComponent]
            })
                .overrideTemplate(PlanningUnitGroupSetDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanningUnitGroupSetDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanningUnitGroupSetService);
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
