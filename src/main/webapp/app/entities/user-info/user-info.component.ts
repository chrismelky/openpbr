import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IUserInfo } from 'app/shared/model/user-info.model';
import { AccountService } from 'app/core';
import { UserInfoService } from './user-info.service';

@Component({
    selector: 'pbr-user-info',
    templateUrl: './user-info.component.html'
})
export class UserInfoComponent implements OnInit, OnDestroy {
    userInfos: IUserInfo[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected userInfoService: UserInfoService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.userInfoService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IUserInfo[]>) => res.ok),
                    map((res: HttpResponse<IUserInfo[]>) => res.body)
                )
                .subscribe((res: IUserInfo[]) => (this.userInfos = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.userInfoService
            .query()
            .pipe(
                filter((res: HttpResponse<IUserInfo[]>) => res.ok),
                map((res: HttpResponse<IUserInfo[]>) => res.body)
            )
            .subscribe(
                (res: IUserInfo[]) => {
                    this.userInfos = res;
                    this.currentSearch = '';
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInUserInfos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IUserInfo) {
        return item.id;
    }

    registerChangeInUserInfos() {
        this.eventSubscriber = this.eventManager.subscribe('userInfoListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
