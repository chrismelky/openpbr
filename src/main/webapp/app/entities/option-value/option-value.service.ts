import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOptionValue } from 'app/shared/model/option-value.model';

type EntityResponseType = HttpResponse<IOptionValue>;
type EntityArrayResponseType = HttpResponse<IOptionValue[]>;

@Injectable({ providedIn: 'root' })
export class OptionValueService {
    public resourceUrl = SERVER_API_URL + 'api/option-values';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/option-values';

    constructor(protected http: HttpClient) {}

    create(optionValue: IOptionValue): Observable<EntityResponseType> {
        return this.http.post<IOptionValue>(this.resourceUrl, optionValue, { observe: 'response' });
    }

    update(optionValue: IOptionValue): Observable<EntityResponseType> {
        return this.http.put<IOptionValue>(this.resourceUrl, optionValue, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IOptionValue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOptionValue[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOptionValue[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
