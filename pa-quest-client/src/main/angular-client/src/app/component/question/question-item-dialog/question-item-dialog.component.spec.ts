import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionItemDialogComponent } from './question-item-dialog.component';

describe('QuestionItemDialogComponent', () => {
  let component: QuestionItemDialogComponent;
  let fixture: ComponentFixture<QuestionItemDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QuestionItemDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QuestionItemDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
