import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnswerSelectDialogComponent } from './answer-select-dialog.component';

describe('AnswerSelectDialogComponent', () => {
  let component: AnswerSelectDialogComponent;
  let fixture: ComponentFixture<AnswerSelectDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnswerSelectDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnswerSelectDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
