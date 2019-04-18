import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { OrganisationUnit } from 'app/shared/model/organisation-unit.model';
import { OrganisationUnitService } from './organisation-unit.service';
import { OrganisationUnitComponent } from './organisation-unit.component';
import { OrganisationUnitDetailComponent } from './organisation-unit-detail.component';
import { OrganisationUnitUpdateComponent } from './organisation-unit-update.component';
import { OrganisationUnitDeletePopupComponent } from './organisation-unit-delete-dialog.component';
import { IOrganisationUnit } from 'app/shared/model/organisation-unit.model';

@Injectable({ providedIn: 'root' })
export class OrganisationUnitResolve implements Resolve<IOrganisationUnit> {
    constructor(private service: OrganisationUnitService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IOrganisationUnit> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<OrganisationUnit>) => response.ok),
                map((organisationUnit: HttpResponse<OrganisationUnit>) => organisationUnit.body)
            );
        }
        return of(new OrganisationUnit());
    }
}

export const organisationUnitRoute: Routes = [
    {
        path: '',
        component: OrganisationUnitComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openpbrApp.organisationUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: OrganisationUnitDetailComponent,
        resolve: {
            organisationUnit: OrganisationUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.organisationUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: OrganisationUnitUpdateComponent,
        resolve: {
            organisationUnit: OrganisationUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.organisationUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: OrganisationUnitUpdateComponent,
        resolve: {
            organisationUnit: OrganisationUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.organisationUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const organisationUnitPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: OrganisationUnitDeletePopupComponent,
        resolve: {
            organisationUnit: OrganisationUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.organisationUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
