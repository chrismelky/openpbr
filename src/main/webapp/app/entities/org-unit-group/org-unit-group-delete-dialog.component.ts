import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrgUnitGroup } from 'app/shared/model/org-unit-group.model';
import { OrgUnitGroupService } from './org-unit-group.service';

@Component({
    selector: 'pbr-org-unit-group-delete-dialog',
    templateUrl: './org-unit-group-delete-dialog.component.html'
})
export class OrgUnitGroupDeleteDialogComponent {
    orgUnitGroup: IOrgUnitGroup;

    constructor(
        protected orgUnitGroupService: OrgUnitGroupService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.orgUnitGroupService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'orgUnitGroupListModification',
                content: 'Deleted an orgUnitGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-org-unit-group-delete-popup',
    template: ''
})
export class OrgUnitGroupDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orgUnitGroup }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OrgUnitGroupDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.orgUnitGroup = orgUnitGroup;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/org-unit-group', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/org-unit-group', { outlets: { popup: null } }]);
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
