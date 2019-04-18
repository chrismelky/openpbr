import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrganisationUnit } from 'app/shared/model/organisation-unit.model';

type EntityResponseType = HttpResponse<IOrganisationUnit>;
type EntityArrayResponseType = HttpResponse<IOrganisationUnit[]>;

@Injectable({ providedIn: 'root' })
export class OrganisationUnitService {
    public resourceUrl = SERVER_API_URL + 'api/organisation-units';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/organisation-units';

    constructor(protected http: HttpClient) {}

    create(organisationUnit: IOrganisationUnit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(organisationUnit);
        return this.http
            .post<IOrganisationUnit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(organisationUnit: IOrganisationUnit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(organisationUnit);
        return this.http
            .put<IOrganisationUnit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IOrganisationUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrganisationUnit[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrganisationUnit[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(organisationUnit: IOrganisationUnit): IOrganisationUnit {
        const copy: IOrganisationUnit = Object.assign({}, organisationUnit, {
            openingDate:
                organisationUnit.openingDate != null && organisationUnit.openingDate.isValid()
                    ? organisationUnit.openingDate.format(DATE_FORMAT)
                    : null,
            closedDate:
                organisationUnit.closedDate != null && organisationUnit.closedDate.isValid()
                    ? organisationUnit.closedDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.openingDate = res.body.openingDate != null ? moment(res.body.openingDate) : null;
            res.body.closedDate = res.body.closedDate != null ? moment(res.body.closedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((organisationUnit: IOrganisationUnit) => {
                organisationUnit.openingDate = organisationUnit.openingDate != null ? moment(organisationUnit.openingDate) : null;
                organisationUnit.closedDate = organisationUnit.closedDate != null ? moment(organisationUnit.closedDate) : null;
            });
        }
        return res;
    }
}
