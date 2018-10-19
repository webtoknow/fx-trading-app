import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BlotterViewComponent } from './blotter-view.component';

describe('BlotterViewComponent', () => {
  let component: BlotterViewComponent;
  let fixture: ComponentFixture<BlotterViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BlotterViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlotterViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
