import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPlanningUnitLevel } from 'app/shared/model/planning-unit-level.model';

type EntityResponseType = HttpResponse<IPlanningUnitLevel>;
type EntityArrayResponseType = HttpResponse<IPlanningUnitLevel[]>;

@Injectable({ providedIn: 'root' })
export class PlanningUnitLevelService {
    public resourceUrl = SERVER_API_URL + 'api/planning-unit-levels';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/planning-unit-levels';

    constructor(protected http: HttpClient) {}

    create(planningUnitLevel: IPlanningUnitLevel): Observable<EntityResponseType> {
        return this.http.post<IPlanningUnitLevel>(this.resourceUrl, planningUnitLevel, { observe: 'response' });
    }

    update(planningUnitLevel: IPlanningUnitLevel): Observable<EntityResponseType> {
        return this.http.put<IPlanningUnitLevel>(this.resourceUrl, planningUnitLevel, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPlanningUnitLevel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPlanningUnitLevel[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPlanningUnitLevel[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
