import { TestBed } from '@angular/core/testing';
import { ApiInterceptor } from './api-interceptor.service';


describe('ApiInterceptorService', () => {
  let service: ApiInterceptor;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApiInterceptor);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
