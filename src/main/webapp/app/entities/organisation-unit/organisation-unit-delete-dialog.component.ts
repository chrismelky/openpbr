import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrganisationUnit } from 'app/shared/model/organisation-unit.model';
import { OrganisationUnitService } from './organisation-unit.service';

@Component({
    selector: 'pbr-organisation-unit-delete-dialog',
    templateUrl: './organisation-unit-delete-dialog.component.html'
})
export class OrganisationUnitDeleteDialogComponent {
    organisationUnit: IOrganisationUnit;

    constructor(
        protected organisationUnitService: OrganisationUnitService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.organisationUnitService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'organisationUnitListModification',
                content: 'Deleted an organisationUnit'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-organisation-unit-delete-popup',
    template: ''
})
export class OrganisationUnitDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ organisationUnit }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OrganisationUnitDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.organisationUnit = organisationUnit;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/organisation-unit', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/organisation-unit', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
