import { Route } from '@angular/router';

import { PbrTrackerComponent } from './tracker.component';

export const trackerRoute: Route = {
    path: 'pbr-tracker',
    component: PbrTrackerComponent,
    data: {
        pageTitle: 'tracker.title'
    }
};
