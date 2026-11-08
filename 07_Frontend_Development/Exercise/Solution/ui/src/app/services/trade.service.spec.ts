import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { backendUrl } from '../constants';
import { Rate } from '../models/rate';

import { TradeService } from './trade.service';

describe('TradeService', () => {
  let service: TradeService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(TradeService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('keeps polling transactions after a temporary failure and stops on unsubscribe', fakeAsync(() => {
    const received: unknown[] = [];
    const subscription = service.getTransactionsPolling().subscribe(rows => received.push(rows));
    http.expectOne(backendUrl.fxTradeService.getTransactions).flush([], { status: 503, statusText: 'Unavailable' });
    tick(2000);
    http.expectOne(backendUrl.fxTradeService.getTransactions).flush([]);
    expect(received).toEqual([[]]);
    subscription.unsubscribe();
    tick(2000);
    http.expectNone(backendUrl.fxTradeService.getTransactions);
  }));

  it('clears failed quotes and recovers on the next poll with the same pair', fakeAsync(() => {
    const received: (Rate | null)[] = [];
    const subscription = service.getFxRatePolling('EUR', 'USD').subscribe(rate => received.push(rate));
    const request = () => http.expectOne(r => r.url === backendUrl.quoteService.getFxRate &&
      r.params.get('primaryCcy') === 'EUR' && r.params.get('secondaryCcy') === 'USD');
    const rate = { buyRate: 1.2, sellRate: 1.1, ts: '2026-11-10T00:00:00Z' };
    request().flush(rate);
    tick(2000);
    request().flush(null, { status: 503, statusText: 'Unavailable' });
    tick(2000);
    request().flush(rate);
    expect(received).toEqual([rate, null, rate]);
    subscription.unsubscribe();
  }));

  it('retries the currency list when the quote service starts late', fakeAsync(() => {
    const received: string[][] = [];
    service.getCurrencies().subscribe(currencies => received.push(currencies));
    http.expectOne(backendUrl.quoteService.getCurrencies).error(new ProgressEvent('error'));
    tick(2000);
    http.expectOne(backendUrl.quoteService.getCurrencies).flush(['EUR', 'USD']);
    expect(received).toEqual([['EUR', 'USD']]);
  }));
});
