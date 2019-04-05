import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'pbr-add-button',
    template: `
        <div class="float-btn">
            <button mat-fab aria-label="Add new user"><mat-icon>add</mat-icon></button>
        </div>
    `,
    styles: []
})
export class AddButtonComponent implements OnInit {
    constructor() {}

    ngOnInit() {}
}
