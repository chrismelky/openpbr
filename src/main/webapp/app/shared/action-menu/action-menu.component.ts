import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';

@Component({
    selector: 'pbr-action-menu',
    templateUrl: './action-menu.component.html',
    styles: []
})
export class ActionMenuComponent implements OnInit, OnChanges {
    @Input() objectName: string;
    @Input() object: any;

    constructor() {}

    ngOnInit() {
        this.checkRequired();
    }

    checkRequired() {
        if (this.objectName === undefined) {
            throw new Error('object name is required');
        }
        if (this.object === undefined) {
            throw new Error('object is required');
        }
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.checkRequired();
    }
}
