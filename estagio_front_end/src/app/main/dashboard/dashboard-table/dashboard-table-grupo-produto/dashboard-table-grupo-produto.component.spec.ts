import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardTableGrupoProdutoComponent } from './dashboard-table-grupo-produto.component';

describe('DashboardTableGrupoProdutoComponent', () => {
  let component: DashboardTableGrupoProdutoComponent;
  let fixture: ComponentFixture<DashboardTableGrupoProdutoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashboardTableGrupoProdutoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DashboardTableGrupoProdutoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
