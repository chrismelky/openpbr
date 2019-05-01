import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';

type EntityResponseType = HttpResponse<IPlanningUnitGroup>;
type EntityArrayResponseType = HttpResponse<IPlanningUnitGroup[]>;

@Injectable({ providedIn: 'root' })
export class PlanningUnitGroupService {
    public resourceUrl = SERVER_API_URL + 'api/planning-unit-groups';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/planning-unit-groups';

    constructor(protected http: HttpClient) {}

    create(planningUnitGroup: IPlanningUnitGroup): Observable<EntityResponseType> {
        return this.http.post<IPlanningUnitGroup>(this.resourceUrl, planningUnitGroup, { observe: 'response' });
    }

    update(planningUnitGroup: IPlanningUnitGroup): Observable<EntityResponseType> {
        return this.http.put<IPlanningUnitGroup>(this.resourceUrl, planningUnitGroup, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPlanningUnitGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPlanningUnitGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPlanningUnitGroup[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
