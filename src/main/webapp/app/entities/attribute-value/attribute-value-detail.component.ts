import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttributeValue } from 'app/shared/model/attribute-value.model';

@Component({
    selector: 'pbr-attribute-value-detail',
    templateUrl: './attribute-value-detail.component.html'
})
export class AttributeValueDetailComponent implements OnInit {
    attributeValue: IAttributeValue;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ attributeValue }) => {
            this.attributeValue = attributeValue;
        });
    }

    previousState() {
        window.history.back();
    }
}
