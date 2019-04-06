import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { OptionValue } from 'app/shared/model/option-value.model';
import { OptionValueService } from './option-value.service';
import { OptionValueComponent } from './option-value.component';
import { OptionValueDetailComponent } from './option-value-detail.component';
import { OptionValueUpdateComponent } from './option-value-update.component';
import { OptionValueDeletePopupComponent } from './option-value-delete-dialog.component';
import { IOptionValue } from 'app/shared/model/option-value.model';

@Injectable({ providedIn: 'root' })
export class OptionValueResolve implements Resolve<IOptionValue> {
    constructor(private service: OptionValueService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IOptionValue> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<OptionValue>) => response.ok),
                map((optionValue: HttpResponse<OptionValue>) => optionValue.body)
            );
        }
        return of(new OptionValue());
    }
}

export const optionValueRoute: Routes = [
    {
        path: '',
        component: OptionValueComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openpbrApp.optionValue.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: OptionValueDetailComponent,
        resolve: {
            optionValue: OptionValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.optionValue.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: OptionValueUpdateComponent,
        resolve: {
            optionValue: OptionValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.optionValue.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: OptionValueUpdateComponent,
        resolve: {
            optionValue: OptionValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.optionValue.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const optionValuePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: OptionValueDeletePopupComponent,
        resolve: {
            optionValue: OptionValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.optionValue.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
