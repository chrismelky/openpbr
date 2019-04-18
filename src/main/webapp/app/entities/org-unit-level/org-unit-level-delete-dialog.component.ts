import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrgUnitLevel } from 'app/shared/model/org-unit-level.model';
import { OrgUnitLevelService } from './org-unit-level.service';

@Component({
    selector: 'pbr-org-unit-level-delete-dialog',
    templateUrl: './org-unit-level-delete-dialog.component.html'
})
export class OrgUnitLevelDeleteDialogComponent {
    orgUnitLevel: IOrgUnitLevel;

    constructor(
        protected orgUnitLevelService: OrgUnitLevelService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.orgUnitLevelService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'orgUnitLevelListModification',
                content: 'Deleted an orgUnitLevel'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-org-unit-level-delete-popup',
    template: ''
})
export class OrgUnitLevelDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orgUnitLevel }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OrgUnitLevelDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.orgUnitLevel = orgUnitLevel;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/org-unit-level', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/org-unit-level', { outlets: { popup: null } }]);
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
