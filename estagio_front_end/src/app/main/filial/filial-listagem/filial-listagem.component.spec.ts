import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilialListagemComponent } from './filial-listagem.component';

describe('FilialListagemComponent', () => {
  let component: FilialListagemComponent;
  let fixture: ComponentFixture<FilialListagemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FilialListagemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FilialListagemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
