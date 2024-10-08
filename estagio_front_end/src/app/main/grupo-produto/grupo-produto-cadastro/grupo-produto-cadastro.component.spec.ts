import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GrupoProdutoCadastroComponent } from './grupo-produto-cadastro.component';

describe('GrupoProdutoCadastroComponent', () => {
  let component: GrupoProdutoCadastroComponent;
  let fixture: ComponentFixture<GrupoProdutoCadastroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GrupoProdutoCadastroComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GrupoProdutoCadastroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
