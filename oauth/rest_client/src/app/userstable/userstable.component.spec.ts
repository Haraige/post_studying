import {ComponentFixture, TestBed} from '@angular/core/testing';

import {UserstableComponent} from './userstable.component';

describe('InterpComponent', () => {
  let component: UserstableComponent;
  let fixture: ComponentFixture<UserstableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserstableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserstableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
