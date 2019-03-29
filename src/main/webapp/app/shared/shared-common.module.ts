import { NgModule } from '@angular/core';

import { OpenpbrSharedLibsModule, FindLanguageFromKeyPipe, PbrAlertComponent, PbrAlertErrorComponent } from './';

@NgModule({
    imports: [OpenpbrSharedLibsModule],
    declarations: [FindLanguageFromKeyPipe, PbrAlertComponent, PbrAlertErrorComponent],
    exports: [OpenpbrSharedLibsModule, FindLanguageFromKeyPipe, PbrAlertComponent, PbrAlertErrorComponent]
})
export class OpenpbrSharedCommonModule {}
