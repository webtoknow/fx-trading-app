import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { backendUrl } from '../../../constants';
import { Transaction } from '../../../models/transaction';

import { BlotterViewComponent } from './blotter-view.component';

function transaction(
  id: number,
  primaryCcy: string,
  secondaryCcy: string,
  date: Date
): Transaction {
  return {
    id,
    username: 'ana',
    primaryCcy,
    secondaryCcy,
    rate: 1.1,
    action: 'BUY',
    notional: 1000,
    tenor: 'SP',
    date: date.getTime(),
  };
}

describe('BlotterViewComponent', () => {
  let component: BlotterViewComponent;
  let fixture: ComponentFixture<BlotterViewComponent>;
  let http: HttpTestingController;

  // 5 March 23:59 and 6 March 00:01 are two minutes apart but on different days.
  // 13 March is the same weekday as 6 March (the old getDay() comparison matched them).
  const data = [
    transaction(1, 'EUR', 'USD', new Date(2026, 2, 5, 23, 59)),
    transaction(2, 'EUR', 'USD', new Date(2026, 2, 6, 0, 1)),
    transaction(3, 'GBP', 'RON', new Date(2026, 2, 6, 12, 0)),
    transaction(4, 'EUR', 'USD', new Date(2026, 2, 13, 12, 0)),
  ];

  const ids = () => component.rows().map((row) => row.id);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BlotterViewComponent, FormsModule, BsDatepickerModule],
      providers: [provideHttpClient(), provideHttpClientTesting()],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(BlotterViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    http
      .expectOne((r) => r.url === backendUrl.fxTradeService.getTransactions)
      .flush(data);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('shows every transaction and the distinct pairs when no filter is set', () => {
    expect(ids()).toEqual([1, 2, 3, 4]);
    expect(component.pairs()).toEqual(['EUR/USD', 'GBP/RON']);
  });

  it('filters by currency pair', () => {
    component.ccyPair.set('GBP/RON');
    expect(ids()).toEqual([3]);

    component.ccyPair.set('EUR/USD');
    expect(ids()).toEqual([1, 2, 4]);

    component.ccyPair.set('');
    expect(ids()).toEqual([1, 2, 3, 4]);
  });

  it('filters by day of the month: 23:59 and 00:01 the next day are different days', () => {
    component.date.set(new Date(2026, 2, 5, 8, 0));
    expect(ids()).toEqual([1]);

    component.date.set(new Date(2026, 2, 6, 17, 30));
    expect(ids()).toEqual([2, 3]);
  });

  it('does not match another date that has the same weekday', () => {
    component.date.set(new Date(2026, 2, 6));
    expect(ids()).not.toContain(4);
  });

  it('combines the pair and the date filter', () => {
    component.date.set(new Date(2026, 2, 6));
    component.ccyPair.set('EUR/USD');
    expect(ids()).toEqual([2]);
  });

  it('renders the filtered rows', () => {
    component.ccyPair.set('GBP/RON');
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('tbody tr').length).toBe(1);
  });
});
