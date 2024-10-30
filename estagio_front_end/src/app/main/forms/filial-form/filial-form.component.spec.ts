import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilialFormComponent } from './filial-form.component';

describe('FilialFormComponent', () => {
  let component: FilialFormComponent;
  let fixture: ComponentFixture<FilialFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FilialFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FilialFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
