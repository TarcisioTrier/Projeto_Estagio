import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GrupoProdutoFormComponent } from './grupo-produto-form.component';

describe('GrupoProdutoFormComponent', () => {
  let component: GrupoProdutoFormComponent;
  let fixture: ComponentFixture<GrupoProdutoFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GrupoProdutoFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GrupoProdutoFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
