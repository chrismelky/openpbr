/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitDeleteDialogComponent } from 'app/entities/planning-unit/planning-unit-delete-dialog.component';
import { PlanningUnitService } from 'app/entities/planning-unit/planning-unit.service';

describe('Component Tests', () => {
    describe('PlanningUnit Management Delete Component', () => {
        let comp: PlanningUnitDeleteDialogComponent;
        let fixture: ComponentFixture<PlanningUnitDeleteDialogComponent>;
        let service: PlanningUnitService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitDeleteDialogComponent]
            })
                .overrideTemplate(PlanningUnitDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanningUnitDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanningUnitService);
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
