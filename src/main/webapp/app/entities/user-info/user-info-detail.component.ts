import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserInfo } from 'app/shared/model/user-info.model';

@Component({
    selector: 'pbr-user-info-detail',
    templateUrl: './user-info-detail.component.html'
})
export class UserInfoDetailComponent implements OnInit {
    userInfo: IUserInfo;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userInfo }) => {
            this.userInfo = userInfo;
        });
    }

    previousState() {
        window.history.back();
    }
}
