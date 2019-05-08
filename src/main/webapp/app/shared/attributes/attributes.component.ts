import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { AttributeValue } from 'app/shared/model/attribute-value.model';
import { Attribute } from 'app/shared/model/attribute.model';

@Component({
    selector: 'pbr-attributes',
    templateUrl: './attributes.component.html',
    styles: []
})
export class AttributesComponent implements OnInit {
    attributeValues: AttributeValue[] = [];
    @Input() attrs: Attribute[] = [];
    @Input() initialValues = [];
    @Output() attributeValueOut: EventEmitter<any> = new EventEmitter<any>();
    @ViewChild('attrForm') attrForm;

    constructor() {}

    ngOnInit() {
        if (this.attrs) {
            this.attrs.forEach(att => {
                const exist = this.initialValues.find(a => {
                    return a.attribute.id === att.id;
                });
                if (exist) {
                    this.attributeValues.push(exist);
                } else {
                    const newAtt: AttributeValue = { attribute: att, value: null };
                    this.attributeValues.push(newAtt);
                }
            });
        }
        this.attributeValueOut.emit({ form: this.attrForm, data: this.attributeValues });
    }

    attrValueChange() {
        this.attributeValueOut.emit({ form: this.attrForm, data: this.attributeValues.filter(i => i.value !== null) });
    }
}
