import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserItemDialogComponent } from './user-item-dialog.component';

describe('UserItemDialogComponent', () => {
  let component: UserItemDialogComponent;
  let fixture: ComponentFixture<UserItemDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserItemDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserItemDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
