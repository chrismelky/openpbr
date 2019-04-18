import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrgUnitGroupSet } from 'app/shared/model/org-unit-group-set.model';
import { OrgUnitGroupSetService } from './org-unit-group-set.service';

@Component({
    selector: 'pbr-org-unit-group-set-delete-dialog',
    templateUrl: './org-unit-group-set-delete-dialog.component.html'
})
export class OrgUnitGroupSetDeleteDialogComponent {
    orgUnitGroupSet: IOrgUnitGroupSet;

    constructor(
        protected orgUnitGroupSetService: OrgUnitGroupSetService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.orgUnitGroupSetService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'orgUnitGroupSetListModification',
                content: 'Deleted an orgUnitGroupSet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-org-unit-group-set-delete-popup',
    template: ''
})
export class OrgUnitGroupSetDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orgUnitGroupSet }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OrgUnitGroupSetDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.orgUnitGroupSet = orgUnitGroupSet;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/org-unit-group-set', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/org-unit-group-set', { outlets: { popup: null } }]);
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
