import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GrupoProdutoComponent } from './grupo-produto.component';

describe('GrupoProdutoComponent', () => {
  let component: GrupoProdutoComponent;
  let fixture: ComponentFixture<GrupoProdutoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GrupoProdutoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GrupoProdutoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
