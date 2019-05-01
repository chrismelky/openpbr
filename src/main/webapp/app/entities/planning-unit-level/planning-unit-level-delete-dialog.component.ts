import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlanningUnitLevel } from 'app/shared/model/planning-unit-level.model';
import { PlanningUnitLevelService } from './planning-unit-level.service';

@Component({
    selector: 'pbr-planning-unit-level-delete-dialog',
    templateUrl: './planning-unit-level-delete-dialog.component.html'
})
export class PlanningUnitLevelDeleteDialogComponent {
    planningUnitLevel: IPlanningUnitLevel;

    constructor(
        protected planningUnitLevelService: PlanningUnitLevelService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.planningUnitLevelService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'planningUnitLevelListModification',
                content: 'Deleted an planningUnitLevel'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-planning-unit-level-delete-popup',
    template: ''
})
export class PlanningUnitLevelDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planningUnitLevel }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PlanningUnitLevelDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.planningUnitLevel = planningUnitLevel;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/planning-unit-level', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/planning-unit-level', { outlets: { popup: null } }]);
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
