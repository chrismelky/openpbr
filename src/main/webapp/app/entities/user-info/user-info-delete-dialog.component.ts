import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserInfo } from 'app/shared/model/user-info.model';
import { UserInfoService } from './user-info.service';

@Component({
    selector: 'pbr-user-info-delete-dialog',
    templateUrl: './user-info-delete-dialog.component.html'
})
export class UserInfoDeleteDialogComponent {
    userInfo: IUserInfo;

    constructor(protected userInfoService: UserInfoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userInfoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'userInfoListModification',
                content: 'Deleted an userInfo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pbr-user-info-delete-popup',
    template: ''
})
export class UserInfoDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userInfo }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(UserInfoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.userInfo = userInfo;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/user-info', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/user-info', { outlets: { popup: null } }]);
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
