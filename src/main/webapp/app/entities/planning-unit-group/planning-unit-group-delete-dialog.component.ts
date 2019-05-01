import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';
import { PlanningUnitGroupService } from './planning-unit-group.service';

@Component({
    selector: 'pbr-planning-unit-group-delete-dialog',
    templateUrl: './planning-unit-group-delete-dialog.component.html'
})
export class PlanningUnitGroupDeleteDialogComponent {
    planningUnitGroup: IPlanningUnitGroup;

    constructor(
        protected planningUnitGroupService: PlanningUnitGroupService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.planningUnitGroupService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'planningUnitGroupListModification',
                content: 'Deleted an planningUnitGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-planning-unit-group-delete-popup',
    template: ''
})
export class PlanningUnitGroupDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planningUnitGroup }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PlanningUnitGroupDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.planningUnitGroup = planningUnitGroup;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/planning-unit-group', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/planning-unit-group', { outlets: { popup: null } }]);
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
