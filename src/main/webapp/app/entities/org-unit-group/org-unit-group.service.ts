import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrgUnitGroup } from 'app/shared/model/org-unit-group.model';

type EntityResponseType = HttpResponse<IOrgUnitGroup>;
type EntityArrayResponseType = HttpResponse<IOrgUnitGroup[]>;

@Injectable({ providedIn: 'root' })
export class OrgUnitGroupService {
    public resourceUrl = SERVER_API_URL + 'api/org-unit-groups';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/org-unit-groups';

    constructor(protected http: HttpClient) {}

    create(orgUnitGroup: IOrgUnitGroup): Observable<EntityResponseType> {
        return this.http.post<IOrgUnitGroup>(this.resourceUrl, orgUnitGroup, { observe: 'response' });
    }

    update(orgUnitGroup: IOrgUnitGroup): Observable<EntityResponseType> {
        return this.http.put<IOrgUnitGroup>(this.resourceUrl, orgUnitGroup, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IOrgUnitGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrgUnitGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrgUnitGroup[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
