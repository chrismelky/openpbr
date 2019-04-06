import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAttributeValue } from 'app/shared/model/attribute-value.model';

type EntityResponseType = HttpResponse<IAttributeValue>;
type EntityArrayResponseType = HttpResponse<IAttributeValue[]>;

@Injectable({ providedIn: 'root' })
export class AttributeValueService {
    public resourceUrl = SERVER_API_URL + 'api/attribute-values';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/attribute-values';

    constructor(protected http: HttpClient) {}

    create(attributeValue: IAttributeValue): Observable<EntityResponseType> {
        return this.http.post<IAttributeValue>(this.resourceUrl, attributeValue, { observe: 'response' });
    }

    update(attributeValue: IAttributeValue): Observable<EntityResponseType> {
        return this.http.put<IAttributeValue>(this.resourceUrl, attributeValue, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAttributeValue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAttributeValue[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAttributeValue[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
