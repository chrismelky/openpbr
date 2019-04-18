import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { OrgUnitGroup } from 'app/shared/model/org-unit-group.model';
import { OrgUnitGroupService } from './org-unit-group.service';
import { OrgUnitGroupComponent } from './org-unit-group.component';
import { OrgUnitGroupDetailComponent } from './org-unit-group-detail.component';
import { OrgUnitGroupUpdateComponent } from './org-unit-group-update.component';
import { OrgUnitGroupDeletePopupComponent } from './org-unit-group-delete-dialog.component';
import { IOrgUnitGroup } from 'app/shared/model/org-unit-group.model';

@Injectable({ providedIn: 'root' })
export class OrgUnitGroupResolve implements Resolve<IOrgUnitGroup> {
    constructor(private service: OrgUnitGroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IOrgUnitGroup> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<OrgUnitGroup>) => response.ok),
                map((orgUnitGroup: HttpResponse<OrgUnitGroup>) => orgUnitGroup.body)
            );
        }
        return of(new OrgUnitGroup());
    }
}

export const orgUnitGroupRoute: Routes = [
    {
        path: '',
        component: OrgUnitGroupComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openpbrApp.orgUnitGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: OrgUnitGroupDetailComponent,
        resolve: {
            orgUnitGroup: OrgUnitGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: OrgUnitGroupUpdateComponent,
        resolve: {
            orgUnitGroup: OrgUnitGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: OrgUnitGroupUpdateComponent,
        resolve: {
            orgUnitGroup: OrgUnitGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const orgUnitGroupPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: OrgUnitGroupDeletePopupComponent,
        resolve: {
            orgUnitGroup: OrgUnitGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
