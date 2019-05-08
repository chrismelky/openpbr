import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { OrgUnitGroupSet } from 'app/shared/model/org-unit-group-set.model';
import { OrgUnitGroupSetService } from './org-unit-group-set.service';
import { OrgUnitGroupSetComponent } from './org-unit-group-set.component';
import { OrgUnitGroupSetDetailComponent } from './org-unit-group-set-detail.component';
import { OrgUnitGroupSetUpdateComponent } from './org-unit-group-set-update.component';
import { OrgUnitGroupSetDeletePopupComponent } from './org-unit-group-set-delete-dialog.component';
import { IOrgUnitGroupSet } from 'app/shared/model/org-unit-group-set.model';
import { AttributeByTypeResolve } from 'app/entities/attribute';

@Injectable({ providedIn: 'root' })
export class OrgUnitGroupSetResolve implements Resolve<IOrgUnitGroupSet> {
    constructor(private service: OrgUnitGroupSetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IOrgUnitGroupSet> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<OrgUnitGroupSet>) => response.ok),
                map((orgUnitGroupSet: HttpResponse<OrgUnitGroupSet>) => orgUnitGroupSet.body)
            );
        }
        return of(new OrgUnitGroupSet());
    }
}

export const orgUnitGroupSetRoute: Routes = [
    {
        path: '',
        component: OrgUnitGroupSetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openpbrApp.orgUnitGroupSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: OrgUnitGroupSetDetailComponent,
        resolve: {
            orgUnitGroupSet: OrgUnitGroupSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitGroupSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: OrgUnitGroupSetUpdateComponent,
        resolve: {
            orgUnitGroupSet: OrgUnitGroupSetResolve,
            attributes: AttributeByTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitGroupSet.home.title',
            attributeType: { 'isOrgUnitGroupSetAttribute.equals': true, sort: ['sortOrder'] }
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: OrgUnitGroupSetUpdateComponent,
        resolve: {
            orgUnitGroupSet: OrgUnitGroupSetResolve,
            attributes: AttributeByTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitGroupSet.home.title',
            attributeType: { 'isOrgUnitGroupSetAttribute.equals': true, sort: ['sortOrder'] }
        },
        canActivate: [UserRouteAccessService]
    }
];

export const orgUnitGroupSetPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: OrgUnitGroupSetDeletePopupComponent,
        resolve: {
            orgUnitGroupSet: OrgUnitGroupSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.orgUnitGroupSet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
