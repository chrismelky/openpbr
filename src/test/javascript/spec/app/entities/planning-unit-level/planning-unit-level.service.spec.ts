/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { PlanningUnitLevelService } from 'app/entities/planning-unit-level/planning-unit-level.service';
import { IPlanningUnitLevel, PlanningUnitLevel } from 'app/shared/model/planning-unit-level.model';

describe('Service Tests', () => {
    describe('PlanningUnitLevel Service', () => {
        let injector: TestBed;
        let service: PlanningUnitLevelService;
        let httpMock: HttpTestingController;
        let elemDefault: IPlanningUnitLevel;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(PlanningUnitLevelService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new PlanningUnitLevel(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, false);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign({}, elemDefault);
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a PlanningUnitLevel', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new PlanningUnitLevel(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a PlanningUnitLevel', async () => {
                const returnedFromService = Object.assign(
                    {
                        uid: 'BBBBBB',
                        code: 'BBBBBB',
                        name: 'BBBBBB',
                        level: 1,
                        isActive: true
                    },
                    elemDefault
                );

                const expected = Object.assign({}, returnedFromService);
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of PlanningUnitLevel', async () => {
                const returnedFromService = Object.assign(
                    {
                        uid: 'BBBBBB',
                        code: 'BBBBBB',
                        name: 'BBBBBB',
                        level: 1,
                        isActive: true
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
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

            it('should delete a PlanningUnitLevel', async () => {
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
