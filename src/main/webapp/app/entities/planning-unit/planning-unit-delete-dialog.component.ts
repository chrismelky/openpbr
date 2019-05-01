import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlanningUnit } from 'app/shared/model/planning-unit.model';
import { PlanningUnitService } from './planning-unit.service';

@Component({
    selector: 'pbr-planning-unit-delete-dialog',
    templateUrl: './planning-unit-delete-dialog.component.html'
})
export class PlanningUnitDeleteDialogComponent {
    planningUnit: IPlanningUnit;

    constructor(
        protected planningUnitService: PlanningUnitService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.planningUnitService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'planningUnitListModification',
                content: 'Deleted an planningUnit'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-planning-unit-delete-popup',
    template: ''
})
export class PlanningUnitDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planningUnit }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PlanningUnitDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.planningUnit = planningUnit;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/planning-unit', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/planning-unit', { outlets: { popup: null } }]);
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
