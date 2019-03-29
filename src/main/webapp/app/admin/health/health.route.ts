import { Route } from '@angular/router';

import { PbrHealthCheckComponent } from './health.component';

export const healthRoute: Route = {
    path: 'pbr-health',
    component: PbrHealthCheckComponent,
    data: {
        pageTitle: 'health.title'
    }
};
