/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { OrganisationUnitService } from 'app/entities/organisation-unit/organisation-unit.service';
import { IOrganisationUnit, OrganisationUnit } from 'app/shared/model/organisation-unit.model';

describe('Service Tests', () => {
    describe('OrganisationUnit Service', () => {
        let injector: TestBed;
        let service: OrganisationUnitService;
        let httpMock: HttpTestingController;
        let elemDefault: IOrganisationUnit;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(OrganisationUnitService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new OrganisationUnit(
                0,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                0,
                currentDate,
                currentDate,
                'AAAAAAA',
                0,
                0,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                0,
                false
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        openingDate: currentDate.format(DATE_FORMAT),
                        closedDate: currentDate.format(DATE_FORMAT)
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

            it('should create a OrganisationUnit', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        openingDate: currentDate.format(DATE_FORMAT),
                        closedDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        openingDate: currentDate,
                        closedDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new OrganisationUnit(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a OrganisationUnit', async () => {
                const returnedFromService = Object.assign(
                    {
                        uid: 'BBBBBB',
                        code: 'BBBBBB',
                        name: 'BBBBBB',
                        level: 1,
                        openingDate: currentDate.format(DATE_FORMAT),
                        closedDate: currentDate.format(DATE_FORMAT),
                        url: 'BBBBBB',
                        latitude: 1,
                        longitude: 1,
                        address: 'BBBBBB',
                        email: 'BBBBBB',
                        phoneNumner: 'BBBBBB',
                        sortOrder: 1,
                        isActive: true
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        openingDate: currentDate,
                        closedDate: currentDate
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

            it('should return a list of OrganisationUnit', async () => {
                const returnedFromService = Object.assign(
                    {
                        uid: 'BBBBBB',
                        code: 'BBBBBB',
                        name: 'BBBBBB',
                        level: 1,
                        openingDate: currentDate.format(DATE_FORMAT),
                        closedDate: currentDate.format(DATE_FORMAT),
                        url: 'BBBBBB',
                        latitude: 1,
                        longitude: 1,
                        address: 'BBBBBB',
                        email: 'BBBBBB',
                        phoneNumner: 'BBBBBB',
                        sortOrder: 1,
                        isActive: true
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        openingDate: currentDate,
                        closedDate: currentDate
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

            it('should delete a OrganisationUnit', async () => {
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
