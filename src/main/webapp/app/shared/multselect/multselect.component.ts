import { Component, Input, OnInit, EventEmitter, Output, OnChanges, SimpleChanges } from '@angular/core';

@Component({
    selector: 'pbr-multselect',
    templateUrl: './multselect.component.html',
    styles: []
})
export class MultselectComponent implements OnInit, OnChanges {
    @Input() items = [];
    @Input() selectedItems = [];
    @Input() isObject: boolean;
    @Input() title: string;
    @Output() notify: EventEmitter<any> = new EventEmitter<any>();

    itemToSelect = [];
    itemToRemove = [];

    constructor() {}

    ngOnInit(): void {
        if (this.selectedItems == null) {
            this.selectedItems = [];
        }
    }
    selectItem(items) {
        items.forEach(item => {
            const idx = this.items.indexOf(item);
            if (idx !== -1) {
                this.items.splice(idx, 1);
                this.selectedItems.push(item);
                this.itemToSelect = [];
            }
        });
        this.notify.emit(this.selectedItems);
    }

    selectAll() {
        this.items.forEach(item => {
            this.selectedItems.push(item);
        });
        this.items = [];
        this.itemToSelect = [];
        this.notify.emit(this.selectedItems);
    }

    removeItem(items) {
        items.forEach(item => {
            const idx = this.selectedItems.indexOf(item);
            if (idx !== -1) {
                this.selectedItems.splice(idx, 1);
                this.items.push(item);
                this.itemToRemove = [];
            }
        });
        this.notify.emit(this.selectedItems);
    }

    removeAll() {
        this.selectedItems.forEach(item => {
            this.items.push(item);
        });
        this.itemToRemove = [];
        this.selectedItems = [];
        this.notify.emit(this.selectedItems);
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (this.selectedItems != null && (changes['selectedItems'] || changes['items'])) {
            this.selectedItems.forEach(item => {
                const idx = this.items.indexOf(item);
                if (idx !== -1) {
                    this.items.splice(idx, 1);
                }
            });
        }
    }
}
