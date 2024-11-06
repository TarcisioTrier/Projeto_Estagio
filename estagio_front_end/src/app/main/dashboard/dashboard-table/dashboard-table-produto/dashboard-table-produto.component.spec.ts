import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardTableProdutoComponent } from './dashboard-table-produto.component';

describe('DashboardTableProdutoComponent', () => {
  let component: DashboardTableProdutoComponent;
  let fixture: ComponentFixture<DashboardTableProdutoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashboardTableProdutoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DashboardTableProdutoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
