import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AtualizacaoPrecoComponent } from './atualizacao-preco.component';

describe('AtualizacaoPrecoComponent', () => {
  let component: AtualizacaoPrecoComponent;
  let fixture: ComponentFixture<AtualizacaoPrecoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AtualizacaoPrecoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AtualizacaoPrecoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
