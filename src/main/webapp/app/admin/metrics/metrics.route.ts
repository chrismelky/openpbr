import { Route } from '@angular/router';

import { PbrMetricsMonitoringComponent } from './metrics.component';

export const metricsRoute: Route = {
    path: 'pbr-metrics',
    component: PbrMetricsMonitoringComponent,
    data: {
        pageTitle: 'metrics.title'
    }
};
