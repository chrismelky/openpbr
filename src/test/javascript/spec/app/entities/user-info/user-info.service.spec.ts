/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { UserInfoService } from 'app/entities/user-info/user-info.service';
import { IUserInfo, UserInfo, Gender } from 'app/shared/model/user-info.model';

describe('Service Tests', () => {
    describe('UserInfo Service', () => {
        let injector: TestBed;
        let service: UserInfoService;
        let httpMock: HttpTestingController;
        let elemDefault: IUserInfo;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(UserInfoService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new UserInfo(
                0,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                Gender.FEMALE,
                currentDate,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA'
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        birthDay: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a UserInfo', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        birthDay: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        birthDay: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new UserInfo(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a UserInfo', async () => {
                const returnedFromService = Object.assign(
                    {
                        uid: 'BBBBBB',
                        code: 'BBBBBB',
                        lastName: 'BBBBBB',
                        firstName: 'BBBBBB',
                        email: 'BBBBBB',
                        phoneNumber: 'BBBBBB',
                        jobTitle: 'BBBBBB',
                        introduction: 'BBBBBB',
                        gender: 'BBBBBB',
                        birthDay: currentDate.format(DATE_FORMAT),
                        nationality: 'BBBBBB',
                        employer: 'BBBBBB',
                        education: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        birthDay: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of UserInfo', async () => {
                const returnedFromService = Object.assign(
                    {
                        uid: 'BBBBBB',
                        code: 'BBBBBB',
                        lastName: 'BBBBBB',
                        firstName: 'BBBBBB',
                        email: 'BBBBBB',
                        phoneNumber: 'BBBBBB',
                        jobTitle: 'BBBBBB',
                        introduction: 'BBBBBB',
                        gender: 'BBBBBB',
                        birthDay: currentDate.format(DATE_FORMAT),
                        nationality: 'BBBBBB',
                        employer: 'BBBBBB',
                        education: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        birthDay: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a UserInfo', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
