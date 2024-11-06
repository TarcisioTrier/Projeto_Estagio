import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AtualizacaoPrecoProdutoComponent } from './atualizacao-preco-produto.component';

describe('AtualizacaoPrecoProdutoComponent', () => {
  let component: AtualizacaoPrecoProdutoComponent;
  let fixture: ComponentFixture<AtualizacaoPrecoProdutoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AtualizacaoPrecoProdutoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AtualizacaoPrecoProdutoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
