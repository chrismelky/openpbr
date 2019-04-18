import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrgUnitGroupSet } from 'app/shared/model/org-unit-group-set.model';

type EntityResponseType = HttpResponse<IOrgUnitGroupSet>;
type EntityArrayResponseType = HttpResponse<IOrgUnitGroupSet[]>;

@Injectable({ providedIn: 'root' })
export class OrgUnitGroupSetService {
    public resourceUrl = SERVER_API_URL + 'api/org-unit-group-sets';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/org-unit-group-sets';

    constructor(protected http: HttpClient) {}

    create(orgUnitGroupSet: IOrgUnitGroupSet): Observable<EntityResponseType> {
        return this.http.post<IOrgUnitGroupSet>(this.resourceUrl, orgUnitGroupSet, { observe: 'response' });
    }

    update(orgUnitGroupSet: IOrgUnitGroupSet): Observable<EntityResponseType> {
        return this.http.put<IOrgUnitGroupSet>(this.resourceUrl, orgUnitGroupSet, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IOrgUnitGroupSet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrgUnitGroupSet[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrgUnitGroupSet[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
