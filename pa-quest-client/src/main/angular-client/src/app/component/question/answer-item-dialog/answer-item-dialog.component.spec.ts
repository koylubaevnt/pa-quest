import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnswerItemDialogComponent } from './answer-item-dialog.component';

describe('AnswerItemDialogComponent', () => {
  let component: AnswerItemDialogComponent;
  let fixture: ComponentFixture<AnswerItemDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnswerItemDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnswerItemDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
