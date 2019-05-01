import { Injectable } from '@angular/core';
import { BehaviorSubject, merge, Observable } from 'rxjs';
import { NestedTreeControl } from '@angular/cdk/tree';
import { CollectionViewer, SelectionChange } from '@angular/cdk/collections';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';

export class Node {
    id: number;
    name: string;
    isLoading?: boolean;
    expanded: boolean;
    level: number;
    children?: Node[];
    constructor(id, name, level, expanded = false) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.isLoading = false;
        this.expanded = false;
        this.children = [];
        this.expanded = expanded;
    }
}

@Injectable()
export class DynamicDataSource {
    dataChange = new BehaviorSubject<any[]>([]);

    get data(): any[] {
        return this.dataChange.value;
    }
    set data(value: any[]) {
        this.treeControl.dataNodes = value;
        this.dataChange.next(value);
    }

    constructor(private treeControl: NestedTreeControl<any>, private service: any) {}

    connect(collectionViewer: CollectionViewer): Observable<any[]> {
        this.treeControl.expansionModel.changed.subscribe(change => {
            if ((change as SelectionChange<any>).added || (change as SelectionChange<any>).removed) {
                this.handleTreeControl(change as SelectionChange<any>);
            }
        });

        return merge(collectionViewer.viewChange, this.dataChange).pipe(map(() => this.data));
    }

    /** Handle expand/collapse behaviors */
    handleTreeControl(change: SelectionChange<any>) {
        if (change.added) {
            change.added.forEach(node => this.toggleNode(node, true));
        }
        if (change.removed) {
            change.removed
                .slice()
                .reverse()
                .forEach(node => this.toggleNode(node, false));
        }
    }

    /**
     * Toggle the node, remove from display list
     */
    toggleNode(node: any, expand: boolean) {
        if (node.children.length > 0 || !expand) {
            // If no children, or cannot find the node, no op
            return;
        }
        node.isLoading = true;
        this.service
            .query({ 'parentId.equals': node.id, sort: ['name'] })
            .pipe(
                filter((response: HttpResponse<any[]>) => response.ok),
                map((resp: HttpResponse<any[]>) => resp.body)
            )
            .subscribe((children: any[]) => {
                node.children = children.map(i => new Node(i.id, i.name, i.level));
                const newData: any[] = [...this.data];
                this.data = [];
                this.data = newData;
                node.isLoading = false;
            });
    }
}
