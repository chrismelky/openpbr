/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OpenpbrTestModule } from '../../../test.module';
import { OrganisationUnitDeleteDialogComponent } from 'app/entities/organisation-unit/organisation-unit-delete-dialog.component';
import { OrganisationUnitService } from 'app/entities/organisation-unit/organisation-unit.service';

describe('Component Tests', () => {
    describe('OrganisationUnit Management Delete Component', () => {
        let comp: OrganisationUnitDeleteDialogComponent;
        let fixture: ComponentFixture<OrganisationUnitDeleteDialogComponent>;
        let service: OrganisationUnitService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OrganisationUnitDeleteDialogComponent]
            })
                .overrideTemplate(OrganisationUnitDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrganisationUnitDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrganisationUnitService);
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
