import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOptionSet } from 'app/shared/model/option-set.model';
import { OptionSetService } from './option-set.service';

@Component({
    selector: 'pbr-option-set-delete-dialog',
    templateUrl: './option-set-delete-dialog.component.html'
})
export class OptionSetDeleteDialogComponent {
    optionSet: IOptionSet;

    constructor(
        protected optionSetService: OptionSetService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.optionSetService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'optionSetListModification',
                content: 'Deleted an optionSet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-option-set-delete-popup',
    template: ''
})
export class OptionSetDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ optionSet }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OptionSetDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.optionSet = optionSet;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/option-set', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/option-set', { outlets: { popup: null } }]);
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
