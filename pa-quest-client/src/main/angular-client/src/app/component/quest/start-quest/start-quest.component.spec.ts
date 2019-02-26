import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StartQuestComponent } from './start-quest.component';

describe('StartQuestComponent', () => {
  let component: StartQuestComponent;
  let fixture: ComponentFixture<StartQuestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StartQuestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StartQuestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
