import { Injectable } from '@angular/core';
import { BehaviorSubject, merge, Observable } from 'rxjs';
import { NestedTreeControl } from '@angular/cdk/tree';
import { CollectionViewer, SelectionChange } from '@angular/cdk/collections';
import { map } from 'rxjs/operators';

export class OrganisationUnit {
    name: string;
    isLoading?: boolean;
    expanded: false;
    level: number;
    children?: OrganisationUnit[];
}

export const children: OrganisationUnit[] = [{ name: 'Child1', level: 2, expanded: false, children: [] }];

const root2: OrganisationUnit[] = [];

@Injectable()
export class DynamicDataSource {
    dataChange = new BehaviorSubject<OrganisationUnit[]>([]);

    get data(): OrganisationUnit[] {
        return this.dataChange.value;
    }
    set data(value: OrganisationUnit[]) {
        console.log(value);
        this.treeControl.dataNodes = value;
        this.dataChange.next(value);
    }

    constructor(private treeControl: NestedTreeControl<OrganisationUnit>) {}

    connect(collectionViewer: CollectionViewer): Observable<OrganisationUnit[]> {
        this.treeControl.expansionModel.changed.subscribe(change => {
            if ((change as SelectionChange<OrganisationUnit>).added || (change as SelectionChange<OrganisationUnit>).removed) {
                this.handleTreeControl(change as SelectionChange<OrganisationUnit>);
            }
        });

        return merge(collectionViewer.viewChange, this.dataChange).pipe(map(() => this.data));
    }

    /** Handle expand/collapse behaviors */
    handleTreeControl(change: SelectionChange<OrganisationUnit>) {
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
    toggleNode(node: OrganisationUnit, expand: boolean) {
        if (node.children.length > 0 || !expand) {
            // If no children, or cannot find the node, no op
            return;
        }
        node.isLoading = true;
        node.children = this.getChild(node.level);
        setTimeout(() => {
            node.isLoading = false;
            const newData: OrganisationUnit[] = [...this.data];
            this.data = [];
            this.data = newData;
        }, 1000);
    }

    getChild(level) {
        let nl = level + 1;
        let child: OrganisationUnit = { name: `Child 1 of level ${level}`, level: nl, children: [], expanded: false };
        let child2: OrganisationUnit = { name: `Child 2 of level ${level}`, level: nl, children: [], expanded: false };
        return [child, child2];
    }
}
