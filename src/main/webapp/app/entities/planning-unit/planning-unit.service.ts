import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPlanningUnit } from 'app/shared/model/planning-unit.model';

type EntityResponseType = HttpResponse<IPlanningUnit>;
type EntityArrayResponseType = HttpResponse<IPlanningUnit[]>;

@Injectable({ providedIn: 'root' })
export class PlanningUnitService {
    public resourceUrl = SERVER_API_URL + 'api/planning-units';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/planning-units';

    constructor(protected http: HttpClient) {}

    create(planningUnit: IPlanningUnit): Observable<EntityResponseType> {
        return this.http.post<IPlanningUnit>(this.resourceUrl, planningUnit, { observe: 'response' });
    }

    update(planningUnit: IPlanningUnit): Observable<EntityResponseType> {
        return this.http.put<IPlanningUnit>(this.resourceUrl, planningUnit, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPlanningUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPlanningUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPlanningUnit[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
