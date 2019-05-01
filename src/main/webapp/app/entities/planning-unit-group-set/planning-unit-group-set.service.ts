import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPlanningUnitGroupSet } from 'app/shared/model/planning-unit-group-set.model';

type EntityResponseType = HttpResponse<IPlanningUnitGroupSet>;
type EntityArrayResponseType = HttpResponse<IPlanningUnitGroupSet[]>;

@Injectable({ providedIn: 'root' })
export class PlanningUnitGroupSetService {
    public resourceUrl = SERVER_API_URL + 'api/planning-unit-group-sets';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/planning-unit-group-sets';

    constructor(protected http: HttpClient) {}

    create(planningUnitGroupSet: IPlanningUnitGroupSet): Observable<EntityResponseType> {
        return this.http.post<IPlanningUnitGroupSet>(this.resourceUrl, planningUnitGroupSet, { observe: 'response' });
    }

    update(planningUnitGroupSet: IPlanningUnitGroupSet): Observable<EntityResponseType> {
        return this.http.put<IPlanningUnitGroupSet>(this.resourceUrl, planningUnitGroupSet, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPlanningUnitGroupSet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPlanningUnitGroupSet[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPlanningUnitGroupSet[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
