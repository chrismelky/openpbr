import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NestedTreeControl } from '@angular/cdk/tree';
import { DynamicDataSource } from './org-unit-tree-datasource';
import { Node } from './org-unit-tree-datasource';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'pbr-org-unit-tree',
    templateUrl: './org-unit-tree.component.html',
    styles: []
})
export class OrgUnitTreeComponent implements OnInit {
    treeControl: NestedTreeControl<any>;
    dataSource: DynamicDataSource;
    selectedNodes: Node[];
    @Input() treeNodeService: any;
    @Input() rootNode: any;
    @Input() initialSelection: any[] = [];
    @Input() multiple = true;
    @Output() onNodeSelectionChange: EventEmitter<Node[]> = new EventEmitter<Node[]>();

    constructor() {
        this.treeControl = new NestedTreeControl<any>(node => node.children);
    }

    ngOnInit() {
        this.selectedNodes = this.initialSelection.map(i => new Node(i.id, i.name, i.level));
        this.dataSource = new DynamicDataSource(this.treeControl, this.treeNodeService);
        if (this.rootNode) {
            this.dataSource.data = [new Node(this.rootNode.id, this.rootNode.name, this.rootNode.level)];
            return;
        }
        this.treeNodeService
            .query({ 'level.equals': 1, sort: ['name'] })
            .pipe(
                filter((response: HttpResponse<any[]>) => response.ok),
                map((resp: HttpResponse<any[]>) => resp.body)
            )
            .subscribe((defaultRootNodes: any[]) => {
                this.dataSource.data = defaultRootNodes.map(i => new Node(i.id, i.name, i.level));
            });
    }

    toggleSelect(node: Node) {
        if (!this.multiple) {
            this.selectedNodes.length = 0;
            this.selectedNodes.push(node);
            this.onNodeSelectionChange.emit(this.selectedNodes);
            return;
        }
        const idx = this.selectedNodes.map(i => i.id).indexOf(node.id);
        if (idx !== -1) {
            this.selectedNodes.splice(idx, 1);
        } else {
            this.selectedNodes.push(node);
        }
        this.onNodeSelectionChange.emit(this.selectedNodes);
    }

    isSelected(node: Node) {
        const idx = this.selectedNodes.map(i => i.id).indexOf(node.id);
        return idx !== -1;
    }

    hasChild = (_: number, node: any) => node.level <= 5;
}
