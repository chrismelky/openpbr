/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { PlanningUnitGroupService } from 'app/entities/planning-unit-group/planning-unit-group.service';
import { IPlanningUnitGroup, PlanningUnitGroup } from 'app/shared/model/planning-unit-group.model';

describe('Service Tests', () => {
    describe('PlanningUnitGroup Service', () => {
        let injector: TestBed;
        let service: PlanningUnitGroupService;
        let httpMock: HttpTestingController;
        let elemDefault: IPlanningUnitGroup;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(PlanningUnitGroupService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new PlanningUnitGroup(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, false);
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

            it('should create a PlanningUnitGroup', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new PlanningUnitGroup(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a PlanningUnitGroup', async () => {
                const returnedFromService = Object.assign(
                    {
                        uid: 'BBBBBB',
                        code: 'BBBBBB',
                        name: 'BBBBBB',
                        sortOrder: 1,
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

            it('should return a list of PlanningUnitGroup', async () => {
                const returnedFromService = Object.assign(
                    {
                        uid: 'BBBBBB',
                        code: 'BBBBBB',
                        name: 'BBBBBB',
                        sortOrder: 1,
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

            it('should delete a PlanningUnitGroup', async () => {
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
