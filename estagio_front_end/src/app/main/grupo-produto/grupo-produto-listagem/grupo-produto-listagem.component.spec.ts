import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GrupoProdutoListagemComponent } from './grupo-produto-listagem.component';

describe('GrupoProdutoListagemComponent', () => {
  let component: GrupoProdutoListagemComponent;
  let fixture: ComponentFixture<GrupoProdutoListagemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GrupoProdutoListagemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GrupoProdutoListagemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
