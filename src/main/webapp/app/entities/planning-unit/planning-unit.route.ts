import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PlanningUnit } from 'app/shared/model/planning-unit.model';
import { PlanningUnitService } from './planning-unit.service';
import { PlanningUnitComponent } from './planning-unit.component';
import { PlanningUnitDetailComponent } from './planning-unit-detail.component';
import { PlanningUnitUpdateComponent } from './planning-unit-update.component';
import { PlanningUnitDeletePopupComponent } from './planning-unit-delete-dialog.component';
import { IPlanningUnit } from 'app/shared/model/planning-unit.model';

@Injectable({ providedIn: 'root' })
export class PlanningUnitResolve implements Resolve<IPlanningUnit> {
    constructor(private service: PlanningUnitService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlanningUnit> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<PlanningUnit>) => response.ok),
                map((planningUnit: HttpResponse<PlanningUnit>) => planningUnit.body)
            );
        }
        return of(new PlanningUnit());
    }
}

export const planningUnitRoute: Routes = [
    {
        path: '',
        component: PlanningUnitComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openpbrApp.planningUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PlanningUnitDetailComponent,
        resolve: {
            planningUnit: PlanningUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PlanningUnitUpdateComponent,
        resolve: {
            planningUnit: PlanningUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: PlanningUnitUpdateComponent,
        resolve: {
            planningUnit: PlanningUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const planningUnitPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PlanningUnitDeletePopupComponent,
        resolve: {
            planningUnit: PlanningUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
