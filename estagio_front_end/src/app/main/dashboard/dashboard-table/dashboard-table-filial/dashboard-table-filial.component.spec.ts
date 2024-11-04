import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardTableFilialComponent } from './dashboard-table-filial.component';

describe('DashboardTableFilialComponent', () => {
  let component: DashboardTableFilialComponent;
  let fixture: ComponentFixture<DashboardTableFilialComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashboardTableFilialComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DashboardTableFilialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
