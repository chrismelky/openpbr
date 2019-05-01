import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlanningUnitGroupSet } from 'app/shared/model/planning-unit-group-set.model';
import { PlanningUnitGroupSetService } from './planning-unit-group-set.service';

@Component({
    selector: 'pbr-planning-unit-group-set-delete-dialog',
    templateUrl: './planning-unit-group-set-delete-dialog.component.html'
})
export class PlanningUnitGroupSetDeleteDialogComponent {
    planningUnitGroupSet: IPlanningUnitGroupSet;

    constructor(
        protected planningUnitGroupSetService: PlanningUnitGroupSetService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.planningUnitGroupSetService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'planningUnitGroupSetListModification',
                content: 'Deleted an planningUnitGroupSet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-planning-unit-group-set-delete-popup',
    template: ''
})
export class PlanningUnitGroupSetDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planningUnitGroupSet }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PlanningUnitGroupSetDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.planningUnitGroupSet = planningUnitGroupSet;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/planning-unit-group-set', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/planning-unit-group-set', { outlets: { popup: null } }]);
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
