import { Component, OnInit } from '@angular/core';
import { NestedTreeControl } from '@angular/cdk/tree';
import { DynamicDataSource, OrganisationUnit } from './org-unit-datasource';
import { SelectionModel } from '@angular/cdk/collections';

const root: OrganisationUnit[] = [
    {
        name: 'Tanzania',
        level: 1,
        expanded: false,
        children: [],
        isLoading: false
    }
];

@Component({
    selector: 'pbr-org-unit',
    templateUrl: './org-unit.component.html',
    styles: []
})
export class OrgUnitComponent implements OnInit {
    treeControl: NestedTreeControl<OrganisationUnit>;
    dataSource: DynamicDataSource;
    checklistSelection = new SelectionModel<OrganisationUnit>(false /* multiple */);

    constructor() {
        this.treeControl = new NestedTreeControl<OrganisationUnit>(node => node.children);
        this.dataSource = new DynamicDataSource(this.treeControl);
        this.dataSource.data = root;
    }

    ngOnInit() {}

    itemSelectionToggle(node: OrganisationUnit): void {
        this.checklistSelection.toggle(node);
    }

    isSelected(node: OrganisationUnit): boolean {
        return this.checklistSelection.isSelected(node);
    }

    hasChild = (_: number, node: OrganisationUnit) => node.level <= 3;
}
