/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OpenpbrTestModule } from '../../../test.module';
import { PlanningUnitLevelDeleteDialogComponent } from 'app/entities/planning-unit-level/planning-unit-level-delete-dialog.component';
import { PlanningUnitLevelService } from 'app/entities/planning-unit-level/planning-unit-level.service';

describe('Component Tests', () => {
    describe('PlanningUnitLevel Management Delete Component', () => {
        let comp: PlanningUnitLevelDeleteDialogComponent;
        let fixture: ComponentFixture<PlanningUnitLevelDeleteDialogComponent>;
        let service: PlanningUnitLevelService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PlanningUnitLevelDeleteDialogComponent]
            })
                .overrideTemplate(PlanningUnitLevelDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanningUnitLevelDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanningUnitLevelService);
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
