import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserInfo } from 'app/shared/model/user-info.model';

type EntityResponseType = HttpResponse<IUserInfo>;
type EntityArrayResponseType = HttpResponse<IUserInfo[]>;

@Injectable({ providedIn: 'root' })
export class UserInfoService {
    public resourceUrl = SERVER_API_URL + 'api/user-infos';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/user-infos';

    constructor(protected http: HttpClient) {}

    create(userInfo: IUserInfo): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(userInfo);
        return this.http
            .post<IUserInfo>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(userInfo: IUserInfo): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(userInfo);
        return this.http
            .put<IUserInfo>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IUserInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IUserInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IUserInfo[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(userInfo: IUserInfo): IUserInfo {
        const copy: IUserInfo = Object.assign({}, userInfo, {
            birthDay: userInfo.birthDay != null && userInfo.birthDay.isValid() ? userInfo.birthDay.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.birthDay = res.body.birthDay != null ? moment(res.body.birthDay) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((userInfo: IUserInfo) => {
                userInfo.birthDay = userInfo.birthDay != null ? moment(userInfo.birthDay) : null;
            });
        }
        return res;
    }
}
