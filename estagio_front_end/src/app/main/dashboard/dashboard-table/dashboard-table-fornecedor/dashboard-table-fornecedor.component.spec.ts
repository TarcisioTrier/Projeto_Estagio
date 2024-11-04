import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardTableFornecedorComponent } from './dashboard-table-fornecedor.component';

describe('DashboardTableFornecedorComponent', () => {
  let component: DashboardTableFornecedorComponent;
  let fixture: ComponentFixture<DashboardTableFornecedorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashboardTableFornecedorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DashboardTableFornecedorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
