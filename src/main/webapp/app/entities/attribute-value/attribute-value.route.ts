import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AttributeValue } from 'app/shared/model/attribute-value.model';
import { AttributeValueService } from './attribute-value.service';
import { AttributeValueComponent } from './attribute-value.component';
import { AttributeValueDetailComponent } from './attribute-value-detail.component';
import { AttributeValueUpdateComponent } from './attribute-value-update.component';
import { AttributeValueDeletePopupComponent } from './attribute-value-delete-dialog.component';
import { IAttributeValue } from 'app/shared/model/attribute-value.model';

@Injectable({ providedIn: 'root' })
export class AttributeValueResolve implements Resolve<IAttributeValue> {
    constructor(private service: AttributeValueService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAttributeValue> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<AttributeValue>) => response.ok),
                map((attributeValue: HttpResponse<AttributeValue>) => attributeValue.body)
            );
        }
        return of(new AttributeValue());
    }
}

export const attributeValueRoute: Routes = [
    {
        path: '',
        component: AttributeValueComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.attributeValue.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AttributeValueDetailComponent,
        resolve: {
            attributeValue: AttributeValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.attributeValue.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AttributeValueUpdateComponent,
        resolve: {
            attributeValue: AttributeValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.attributeValue.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AttributeValueUpdateComponent,
        resolve: {
            attributeValue: AttributeValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.attributeValue.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const attributeValuePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AttributeValueDeletePopupComponent,
        resolve: {
            attributeValue: AttributeValueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'openpbrApp.attributeValue.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
