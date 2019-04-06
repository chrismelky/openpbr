/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenpbrTestModule } from '../../../test.module';
import { AttributeValueDetailComponent } from 'app/entities/attribute-value/attribute-value-detail.component';
import { AttributeValue } from 'app/shared/model/attribute-value.model';

describe('Component Tests', () => {
    describe('AttributeValue Management Detail Component', () => {
        let comp: AttributeValueDetailComponent;
        let fixture: ComponentFixture<AttributeValueDetailComponent>;
        const route = ({ data: of({ attributeValue: new AttributeValue(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenpbrTestModule],
                declarations: [AttributeValueDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AttributeValueDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AttributeValueDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.attributeValue).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
