import { Route } from '@angular/router';

import { PbrDocsComponent } from './docs.component';

export const docsRoute: Route = {
    path: 'docs',
    component: PbrDocsComponent,
    data: {
        pageTitle: 'global.menu.admin.apidocs'
    }
};
