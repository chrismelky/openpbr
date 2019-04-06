import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAttributeValue } from 'app/shared/model/attribute-value.model';
import { AccountService } from 'app/core';
import { AttributeValueService } from './attribute-value.service';

@Component({
    selector: 'pbr-attribute-value',
    templateUrl: './attribute-value.component.html'
})
export class AttributeValueComponent implements OnInit, OnDestroy {
    attributeValues: IAttributeValue[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected attributeValueService: AttributeValueService,
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
            this.attributeValueService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IAttributeValue[]>) => res.ok),
                    map((res: HttpResponse<IAttributeValue[]>) => res.body)
                )
                .subscribe((res: IAttributeValue[]) => (this.attributeValues = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.attributeValueService
            .query()
            .pipe(
                filter((res: HttpResponse<IAttributeValue[]>) => res.ok),
                map((res: HttpResponse<IAttributeValue[]>) => res.body)
            )
            .subscribe(
                (res: IAttributeValue[]) => {
                    this.attributeValues = res;
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
        this.registerChangeInAttributeValues();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAttributeValue) {
        return item.id;
    }

    registerChangeInAttributeValues() {
        this.eventSubscriber = this.eventManager.subscribe('attributeValueListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
