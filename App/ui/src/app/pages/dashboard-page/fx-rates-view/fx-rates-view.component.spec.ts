import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { backendUrl } from '../../../constants';

import { FxRatesViewComponent } from './fx-rates-view.component';

describe('FxRatesViewComponent', () => {
  let component: FxRatesViewComponent;
  let fixture: ComponentFixture<FxRatesViewComponent>;
  let http: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FxRatesViewComponent, FormsModule, ToastrModule.forRoot()],
      providers: [provideHttpClient(), provideHttpClientTesting()],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(FxRatesViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('turns the currencies request into the currencies signal', () => {
    expect(component.currencies()).toEqual([]);
    http.expectOne(backendUrl.quoteService.getCurrencies).flush(['EUR', 'USD']);
    expect(component.currencies()).toEqual(['EUR', 'USD']);
  });

  it('adds a widget by replacing the array, not mutating it', () => {
    const before = component.widgets();
    component.onAddWidget();
    const after = component.widgets();

    expect(after).not.toBe(before);
    expect(before.length).toBe(0);
    expect(after.length).toBe(1);
  });

  it('removes the widget at the given index by replacing the array', () => {
    component.onAddWidget();
    component.onAddWidget();
    component.onAddWidget();
    const before = component.widgets();
    const [first, , third] = before;

    component.onDeleteWidget(1);
    const after = component.widgets();

    expect(after).not.toBe(before);
    expect(before.length).toBe(3);
    expect(after).toEqual([first, third]);
    expect(after[0]).toBe(first);
    expect(after[1]).toBe(third);
  });

  it('renders one app-widget per widget', () => {
    component.onAddWidget();
    component.onAddWidget();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('app-widget').length).toBe(2);
  });
});
