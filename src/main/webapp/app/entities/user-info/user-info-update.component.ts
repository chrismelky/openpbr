import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IUserInfo } from 'app/shared/model/user-info.model';
import { UserInfoService } from './user-info.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'pbr-user-info-update',
    templateUrl: './user-info-update.component.html'
})
export class UserInfoUpdateComponent implements OnInit {
    userInfo: IUserInfo;
    isSaving: boolean;

    users: IUser[];
    birthDayDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected userInfoService: UserInfoService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userInfo }) => {
            this.userInfo = userInfo;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.userInfo.id !== undefined) {
            this.subscribeToSaveResponse(this.userInfoService.update(this.userInfo));
        } else {
            this.subscribeToSaveResponse(this.userInfoService.create(this.userInfo));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserInfo>>) {
        result.subscribe((res: HttpResponse<IUserInfo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
