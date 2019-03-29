/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { OpenpbrTestModule } from '../../../test.module';
import { UserInfoComponent } from 'app/entities/user-info/user-info.component';
import { UserInfoService } from 'app/entities/user-info/user-info.service';
import { UserInfo } from 'app/shared/model/user-info.model';

describe('Component Tests', () => {
    describe('UserInfo Management Component', () => {
        let comp: UserInfoComponent;
        let fixture: ComponentFixture<UserInfoComponent>;
        let service: UserInfoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [UserInfoComponent],
                providers: []
            })
                .overrideTemplate(UserInfoComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserInfoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserInfoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new UserInfo(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.userInfos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
