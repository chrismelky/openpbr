import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JhiLanguageHelper, User, UserService } from 'app/core';
import { UserInfo } from 'app/shared/model/user-info.model';
import { Attribute } from 'app/shared/model/attribute.model';
import { AttributeValue } from 'app/shared/model/attribute-value.model';

@Component({
    selector: 'pbr-user-mgmt-update',
    templateUrl: './user-management-update.component.html'
})
export class UserMgmtUpdateComponent implements OnInit {
    userInfo: UserInfo;
    user: User;
    languages: any[];
    authorities: any[];
    isSaving: boolean;
    userAttributes: Attribute[];
    userInitialAttrValues: AttributeValue[];
    attrForm = { form: { invalid: false } };

    constructor(
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private route: ActivatedRoute,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.route.data.subscribe(({ user, attributes }) => {
            this.user = user.body ? user.body : user;
            this.userAttributes = [...attributes];
            this.userInitialAttrValues = [...this.user.attributeValues];
        });
        this.authorities = [];
        this.userService.authorities().subscribe(authorities => {
            this.authorities = authorities;
        });
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });
    }

    onAttrValueChange(attrValues) {
        this.user.attributeValues = attrValues.data;
        this.attrForm = attrValues.form;
    }

    previousState() {
        window.history.back();
    }

    onAuthoritiesChanged(authorities) {
        this.user.authorities = authorities;
    }

    save() {
        this.isSaving = true;
        if (this.user.id !== null) {
            this.userService.update(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.userService.create(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
