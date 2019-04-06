import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOptionValue } from 'app/shared/model/option-value.model';
import { OptionValueService } from './option-value.service';

@Component({
    selector: 'pbr-option-value-delete-dialog',
    templateUrl: './option-value-delete-dialog.component.html'
})
export class OptionValueDeleteDialogComponent {
    optionValue: IOptionValue;

    constructor(
        protected optionValueService: OptionValueService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.optionValueService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'optionValueListModification',
                content: 'Deleted an optionValue'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-option-value-delete-popup',
    template: ''
})
export class OptionValueDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ optionValue }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OptionValueDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.optionValue = optionValue;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/option-value', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/option-value', { outlets: { popup: null } }]);
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
