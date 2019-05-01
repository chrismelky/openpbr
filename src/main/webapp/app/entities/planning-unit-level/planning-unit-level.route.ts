import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PlanningUnitLevel } from 'app/shared/model/planning-unit-level.model';
import { PlanningUnitLevelService } from './planning-unit-level.service';
import { PlanningUnitLevelComponent } from './planning-unit-level.component';
import { PlanningUnitLevelDetailComponent } from './planning-unit-level-detail.component';
import { PlanningUnitLevelUpdateComponent } from './planning-unit-level-update.component';
import { PlanningUnitLevelDeletePopupComponent } from './planning-unit-level-delete-dialog.component';
import { IPlanningUnitLevel } from 'app/shared/model/planning-unit-level.model';

@Injectable({ providedIn: 'root' })
export class PlanningUnitLevelResolve implements Resolve<IPlanningUnitLevel> {
    constructor(private service: PlanningUnitLevelService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlanningUnitLevel> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<PlanningUnitLevel>) => response.ok),
                map((planningUnitLevel: HttpResponse<PlanningUnitLevel>) => planningUnitLevel.body)
            );
        }
        return of(new PlanningUnitLevel());
    }
}

export const planningUnitLevelRoute: Routes = [
    {
        path: '',
        component: PlanningUnitLevelComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openpbrApp.planningUnitLevel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PlanningUnitLevelDetailComponent,
        resolve: {
            planningUnitLevel: PlanningUnitLevelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitLevel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PlanningUnitLevelUpdateComponent,
        resolve: {
            planningUnitLevel: PlanningUnitLevelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitLevel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: PlanningUnitLevelUpdateComponent,
        resolve: {
            planningUnitLevel: PlanningUnitLevelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitLevel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const planningUnitLevelPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PlanningUnitLevelDeletePopupComponent,
        resolve: {
            planningUnitLevel: PlanningUnitLevelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.planningUnitLevel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
