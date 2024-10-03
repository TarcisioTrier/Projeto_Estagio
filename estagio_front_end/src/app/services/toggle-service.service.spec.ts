import { TestBed } from '@angular/core/testing';

import { ToggleServiceService } from './toggle-service.service';

describe('ToggleServiceService', () => {
  let service: ToggleServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ToggleServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
