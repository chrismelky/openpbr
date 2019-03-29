import { SpyObject } from './spyobject';
import { PbrTrackerService } from 'app/core/tracker/tracker.service';

export class MockTrackerService extends SpyObject {
    constructor() {
        super(PbrTrackerService);
    }

    connect() {}
}
