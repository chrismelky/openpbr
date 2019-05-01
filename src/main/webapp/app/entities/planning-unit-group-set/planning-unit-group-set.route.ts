import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PlanningUnitGroupSet } from 'app/shared/model/planning-unit-group-set.model';
import { PlanningUnitGroupSetService } from './planning-unit-group-set.service';
import { PlanningUnitGroupSetComponent } from './planning-unit-group-set.component';
import { PlanningUnitGroupSetDetailComponent } from './planning-unit-group-set-detail.component';
import { PlanningUnitGroupSetUpdateComponent } from './planning-unit-group-set-update.component';
import { PlanningUnitGroupSetDeletePopupComponent } from './planning-unit-group-set-delete-dialog.component';
import { IPlanningUnitGroupSet } from 'app/shared/model/planning-unit-group-set.model';

@Injectable({ providedIn: 'root' })
export class PlanningUnitGroupSetResolve implements Resolve<IPlanningUnitGroupSet> {
    constructor(private service: PlanningUnitGroupSetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlanningUnitGroupSet> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<PlanningUnitGroupSet>) => response.ok),
                map((planningUnitGroupSet: HttpResponse<PlanningUnitGroupSet>) => planningUnitGroupSet.body)
            );
        }
        return of(new PlanningUnitGroupSet());
    }
}

export const planningUnitGroupSetRoute: Routes = [
    {
        path: '',
        component: PlanningUnitGroupSetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openpbrApp.planningUnitGroupSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PlanningUnitGroupSetDetailComponent,
        resolve: {
            planningUnitGroupSet: PlanningUnitGroupSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitGroupSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PlanningUnitGroupSetUpdateComponent,
        resolve: {
            planningUnitGroupSet: PlanningUnitGroupSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitGroupSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: PlanningUnitGroupSetUpdateComponent,
        resolve: {
            planningUnitGroupSet: PlanningUnitGroupSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitGroupSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const planningUnitGroupSetPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PlanningUnitGroupSetDeletePopupComponent,
        resolve: {
            planningUnitGroupSet: PlanningUnitGroupSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitGroupSet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
