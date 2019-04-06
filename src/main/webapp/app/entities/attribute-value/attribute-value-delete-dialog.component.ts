import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAttributeValue } from 'app/shared/model/attribute-value.model';
import { AttributeValueService } from './attribute-value.service';

@Component({
    selector: 'pbr-attribute-value-delete-dialog',
    templateUrl: './attribute-value-delete-dialog.component.html'
})
export class AttributeValueDeleteDialogComponent {
    attributeValue: IAttributeValue;

    constructor(
        protected attributeValueService: AttributeValueService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.attributeValueService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'attributeValueListModification',
                content: 'Deleted an attributeValue'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-attribute-value-delete-popup',
    template: ''
})
export class AttributeValueDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ attributeValue }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AttributeValueDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.attributeValue = attributeValue;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/attribute-value', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/attribute-value', { outlets: { popup: null } }]);
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
