import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AtualizacaoPrecoGrupoProdutoComponent } from './atualizacao-preco-grupo-produto.component';

describe('AtualizacaoPrecoGrupoProdutoComponent', () => {
  let component: AtualizacaoPrecoGrupoProdutoComponent;
  let fixture: ComponentFixture<AtualizacaoPrecoGrupoProdutoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AtualizacaoPrecoGrupoProdutoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AtualizacaoPrecoGrupoProdutoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
