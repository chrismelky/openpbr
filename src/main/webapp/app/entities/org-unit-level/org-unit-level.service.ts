import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrgUnitLevel } from 'app/shared/model/org-unit-level.model';

type EntityResponseType = HttpResponse<IOrgUnitLevel>;
type EntityArrayResponseType = HttpResponse<IOrgUnitLevel[]>;

@Injectable({ providedIn: 'root' })
export class OrgUnitLevelService {
    public resourceUrl = SERVER_API_URL + 'api/org-unit-levels';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/org-unit-levels';

    constructor(protected http: HttpClient) {}

    create(orgUnitLevel: IOrgUnitLevel): Observable<EntityResponseType> {
        return this.http.post<IOrgUnitLevel>(this.resourceUrl, orgUnitLevel, { observe: 'response' });
    }

    update(orgUnitLevel: IOrgUnitLevel): Observable<EntityResponseType> {
        return this.http.put<IOrgUnitLevel>(this.resourceUrl, orgUnitLevel, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IOrgUnitLevel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrgUnitLevel[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrgUnitLevel[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
