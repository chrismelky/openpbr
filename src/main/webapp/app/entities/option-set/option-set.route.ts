import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { OptionSet } from 'app/shared/model/option-set.model';
import { OptionSetService } from './option-set.service';
import { OptionSetComponent } from './option-set.component';
import { OptionSetDetailComponent } from './option-set-detail.component';
import { OptionSetUpdateComponent } from './option-set-update.component';
import { OptionSetDeletePopupComponent } from './option-set-delete-dialog.component';
import { IOptionSet } from 'app/shared/model/option-set.model';

@Injectable({ providedIn: 'root' })
export class OptionSetResolve implements Resolve<IOptionSet> {
    constructor(private service: OptionSetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IOptionSet> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<OptionSet>) => response.ok),
                map((optionSet: HttpResponse<OptionSet>) => optionSet.body)
            );
        }
        return of(new OptionSet());
    }
}

export const optionSetRoute: Routes = [
    {
        path: '',
        component: OptionSetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openpbrApp.optionSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: OptionSetDetailComponent,
        resolve: {
            optionSet: OptionSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.optionSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: OptionSetUpdateComponent,
        resolve: {
            optionSet: OptionSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.optionSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: OptionSetUpdateComponent,
        resolve: {
            optionSet: OptionSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.optionSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const optionSetPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: OptionSetDeletePopupComponent,
        resolve: {
            optionSet: OptionSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.optionSet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
