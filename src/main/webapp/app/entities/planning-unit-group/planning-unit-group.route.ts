import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';
import { PlanningUnitGroupService } from './planning-unit-group.service';
import { PlanningUnitGroupComponent } from './planning-unit-group.component';
import { PlanningUnitGroupDetailComponent } from './planning-unit-group-detail.component';
import { PlanningUnitGroupUpdateComponent } from './planning-unit-group-update.component';
import { PlanningUnitGroupDeletePopupComponent } from './planning-unit-group-delete-dialog.component';
import { IPlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';

@Injectable({ providedIn: 'root' })
export class PlanningUnitGroupResolve implements Resolve<IPlanningUnitGroup> {
    constructor(private service: PlanningUnitGroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlanningUnitGroup> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<PlanningUnitGroup>) => response.ok),
                map((planningUnitGroup: HttpResponse<PlanningUnitGroup>) => planningUnitGroup.body)
            );
        }
        return of(new PlanningUnitGroup());
    }
}

export const planningUnitGroupRoute: Routes = [
    {
        path: '',
        component: PlanningUnitGroupComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openpbrApp.planningUnitGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PlanningUnitGroupDetailComponent,
        resolve: {
            planningUnitGroup: PlanningUnitGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PlanningUnitGroupUpdateComponent,
        resolve: {
            planningUnitGroup: PlanningUnitGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: PlanningUnitGroupUpdateComponent,
        resolve: {
            planningUnitGroup: PlanningUnitGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const planningUnitGroupPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PlanningUnitGroupDeletePopupComponent,
        resolve: {
            planningUnitGroup: PlanningUnitGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
