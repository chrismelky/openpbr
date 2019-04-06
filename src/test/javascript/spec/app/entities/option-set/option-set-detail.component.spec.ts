/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { OptionSetDetailComponent } from 'app/entities/option-set/option-set-detail.component';
import { OptionSet } from 'app/shared/model/option-set.model';

describe('Component Tests', () => {
    describe('OptionSet Management Detail Component', () => {
        let comp: OptionSetDetailComponent;
        let fixture: ComponentFixture<OptionSetDetailComponent>;
        const route = ({ data: of({ optionSet: new OptionSet(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [OptionSetDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OptionSetDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OptionSetDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.optionSet).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
