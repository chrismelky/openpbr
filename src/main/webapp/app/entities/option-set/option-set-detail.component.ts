import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOptionSet } from 'app/shared/model/option-set.model';

@Component({
    selector: 'pbr-option-set-detail',
    templateUrl: './option-set-detail.component.html'
})
export class OptionSetDetailComponent implements OnInit {
    optionSet: IOptionSet;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ optionSet }) => {
            this.optionSet = optionSet;
        });
    }

    previousState() {
        window.history.back();
    }
}
