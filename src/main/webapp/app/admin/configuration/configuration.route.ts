import { Route } from '@angular/router';

import { PbrConfigurationComponent } from './configuration.component';

export const configurationRoute: Route = {
    path: 'pbr-configuration',
    component: PbrConfigurationComponent,
    data: {
        pageTitle: 'configuration.title'
    }
};
