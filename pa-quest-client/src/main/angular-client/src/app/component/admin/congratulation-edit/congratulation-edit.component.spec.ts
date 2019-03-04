import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CongratulationEditComponent } from './congratulation-edit.component';

describe('CongratulationEditComponent', () => {
  let component: CongratulationEditComponent;
  let fixture: ComponentFixture<CongratulationEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CongratulationEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CongratulationEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
