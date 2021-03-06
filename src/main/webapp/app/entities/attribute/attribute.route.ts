import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Attribute } from 'app/shared/model/attribute.model';
import { AttributeService } from './attribute.service';
import { AttributeComponent } from './attribute.component';
import { AttributeDetailComponent } from './attribute-detail.component';
import { AttributeUpdateComponent } from './attribute-update.component';
import { AttributeDeletePopupComponent } from './attribute-delete-dialog.component';
import { IAttribute } from 'app/shared/model/attribute.model';

@Injectable({ providedIn: 'root' })
export class AttributeResolve implements Resolve<IAttribute> {
    constructor(private service: AttributeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAttribute> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Attribute>) => response.ok),
                map((attribute: HttpResponse<Attribute>) => attribute.body)
            );
        }
        return of(new Attribute());
    }
}

@Injectable({ providedIn: 'root' })
export class AttributeByTypeResolve implements Resolve<IAttribute[]> {
    constructor(private service: AttributeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAttribute[]> {
        const attFilter = route.data['attributeType'];
        if (attFilter) {
            return this.service.query(attFilter).pipe(
                filter((response: HttpResponse<Attribute[]>) => response.ok),
                map((attribute: HttpResponse<Attribute[]>) => attribute.body)
            );
        }
        return of(null);
    }
}

export const attributeRoute: Routes = [
    {
        path: '',
        component: AttributeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'openpbrApp.attribute.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AttributeDetailComponent,
        resolve: {
            attribute: AttributeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.attribute.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AttributeUpdateComponent,
        resolve: {
            attribute: AttributeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.attribute.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AttributeUpdateComponent,
        resolve: {
            attribute: AttributeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.attribute.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const attributePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AttributeDeletePopupComponent,
        resolve: {
            attribute: AttributeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.attribute.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
