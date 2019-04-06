import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOptionValue } from 'app/shared/model/option-value.model';

@Component({
    selector: 'pbr-option-value-detail',
    templateUrl: './option-value-detail.component.html'
})
export class OptionValueDetailComponent implements OnInit {
    optionValue: IOptionValue;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ optionValue }) => {
            this.optionValue = optionValue;
        });
    }

    previousState() {
        window.history.back();
    }
}
