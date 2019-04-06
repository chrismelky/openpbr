import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOptionSet } from 'app/shared/model/option-set.model';

type EntityResponseType = HttpResponse<IOptionSet>;
type EntityArrayResponseType = HttpResponse<IOptionSet[]>;

@Injectable({ providedIn: 'root' })
export class OptionSetService {
    public resourceUrl = SERVER_API_URL + 'api/option-sets';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/option-sets';

    constructor(protected http: HttpClient) {}

    create(optionSet: IOptionSet): Observable<EntityResponseType> {
        return this.http.post<IOptionSet>(this.resourceUrl, optionSet, { observe: 'response' });
    }

    update(optionSet: IOptionSet): Observable<EntityResponseType> {
        return this.http.put<IOptionSet>(this.resourceUrl, optionSet, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IOptionSet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOptionSet[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOptionSet[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
