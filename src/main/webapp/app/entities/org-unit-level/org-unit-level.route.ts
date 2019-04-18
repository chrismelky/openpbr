import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { OrgUnitLevel } from 'app/shared/model/org-unit-level.model';
import { OrgUnitLevelService } from './org-unit-level.service';
import { OrgUnitLevelComponent } from './org-unit-level.component';
import { OrgUnitLevelDetailComponent } from './org-unit-level-detail.component';
import { OrgUnitLevelUpdateComponent } from './org-unit-level-update.component';
import { OrgUnitLevelDeletePopupComponent } from './org-unit-level-delete-dialog.component';
import { IOrgUnitLevel } from 'app/shared/model/org-unit-level.model';

@Injectable({ providedIn: 'root' })
export class OrgUnitLevelResolve implements Resolve<IOrgUnitLevel> {
    constructor(private service: OrgUnitLevelService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IOrgUnitLevel> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<OrgUnitLevel>) => response.ok),
                map((orgUnitLevel: HttpResponse<OrgUnitLevel>) => orgUnitLevel.body)
            );
        }
        return of(new OrgUnitLevel());
    }
}

export const orgUnitLevelRoute: Routes = [
    {
        path: '',
        component: OrgUnitLevelComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openpbrApp.orgUnitLevel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: OrgUnitLevelDetailComponent,
        resolve: {
            orgUnitLevel: OrgUnitLevelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitLevel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: OrgUnitLevelUpdateComponent,
        resolve: {
            orgUnitLevel: OrgUnitLevelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitLevel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: OrgUnitLevelUpdateComponent,
        resolve: {
            orgUnitLevel: OrgUnitLevelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitLevel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const orgUnitLevelPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: OrgUnitLevelDeletePopupComponent,
        resolve: {
            orgUnitLevel: OrgUnitLevelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitLevel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
