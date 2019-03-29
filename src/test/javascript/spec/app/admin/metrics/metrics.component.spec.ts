import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { PbrMetricsMonitoringComponent } from 'app/admin/metrics/metrics.component';
import { PbrMetricsService } from 'app/admin/metrics/metrics.service';

describe('Component Tests', () => {
    describe('PbrMetricsMonitoringComponent', () => {
        let comp: PbrMetricsMonitoringComponent;
        let fixture: ComponentFixture<PbrMetricsMonitoringComponent>;
        let service: PbrMetricsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [PbrMetricsMonitoringComponent]
            })
                .overrideTemplate(PbrMetricsMonitoringComponent, '')
                .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PbrMetricsMonitoringComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PbrMetricsService);
        });

        describe('refresh', () => {
            it('should call refresh on init', () => {
                // GIVEN
                const response = {
                    timers: {
                        service: 'test',
                        unrelatedKey: 'test'
                    },
                    gauges: {
                        'jcache.statistics': {
                            value: 2
                        },
                        unrelatedKey: 'test'
                    }
                };
                spyOn(service, 'getMetrics').and.returnValue(of(response));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.getMetrics).toHaveBeenCalled();
            });
        });
    });
});
