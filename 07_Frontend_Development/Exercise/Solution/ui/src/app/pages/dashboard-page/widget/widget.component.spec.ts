import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  TestRequest,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { backendUrl } from '../../../constants';
import { RateTrend, Widget } from '../../../models/widget';

import { WidgetComponent } from './widget.component';

describe('WidgetComponent', () => {
  let component: WidgetComponent;
  let fixture: ComponentFixture<WidgetComponent>;
  let http: HttpTestingController;

  const rateRequests = (primary: string) =>
    http.match(
      (r) =>
        r.url === backendUrl.quoteService.getFxRate &&
        r.params.get('primaryCcy') === primary
    );

  function flushRate(primary: string, buyRate: number, sellRate: number) {
    const [request] = rateRequests(primary);
    expect(request).withContext(`a rate request for ${primary}`).toBeDefined();
    request.flush({ buyRate, sellRate, ts: 0 });
  }

  function pick(primary: string, secondary: string) {
    component.primaryCcy.set(primary);
    component.secondaryCcy.set(secondary);
    component.onPickCurrency();
    fixture.detectChanges();
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WidgetComponent, FormsModule, ToastrModule.forRoot()],
      providers: [provideHttpClient(), provideHttpClientTesting()],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(WidgetComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('widget', new Widget());
    fixture.componentRef.setInput('index', 0);
    fixture.componentRef.setInput('currencies', ['EUR', 'USD', 'GBP']);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('emits deleted with its index when the close icon is clicked', () => {
    fixture.componentRef.setInput('index', 3);
    const emitted: number[] = [];
    component.deleted.subscribe((index) => emitted.push(index));

    fixture.nativeElement.querySelector('.close').click();

    expect(emitted).toEqual([3]);
  });

  it('does not poll until both currencies are chosen and different', () => {
    component.primaryCcy.set('EUR');
    component.onPickCurrency();
    fixture.detectChanges();
    expect(component.pair()).toBeNull();

    component.secondaryCcy.set('EUR');
    component.onPickCurrency();
    fixture.detectChanges();
    expect(component.pair()).toBeNull();
    http.expectNone((r) => r.url === backendUrl.quoteService.getFxRate);
  });

  it('does not mutate the Widget it was given', () => {
    const widget = fixture.componentRef.instance.widget();
    pick('EUR', 'USD');
    component.notional.set(500);
    expect(widget.primaryCcy).toBe('');
    expect(widget.notional).toBeNull();
    expect(widget.pickCCYState).toBeTrue();
    fixture.destroy();
  });

  it('computes buy and sell trends from two consecutive rates: UP, DOWN, FLAT', fakeAsync(() => {
    pick('EUR', 'USD');

    flushRate('EUR', 1.1, 1.2);
    expect(component.buyTrend()).toBe(RateTrend.FLAT);
    expect(component.sellTrend()).toBe(RateTrend.FLAT);

    tick(2000);
    flushRate('EUR', 1.3, 1.1); // buy up, sell down
    expect(component.buyTrend()).toBe(RateTrend.UP);
    expect(component.sellTrend()).toBe(RateTrend.DOWN);

    tick(2000);
    flushRate('EUR', 1.2, 1.1); // buy down, sell same
    expect(component.buyTrend()).toBe(RateTrend.DOWN);
    expect(component.sellTrend()).toBe(RateTrend.FLAT);

    tick(2000);
    flushRate('EUR', 1.2, 1.1); // nothing changed
    expect(component.buyTrend()).toBe(RateTrend.FLAT);
    expect(component.sellTrend()).toBe(RateTrend.FLAT);

    fixture.destroy();
  }));

  it('draws the BUY arrow from the buy trend and the SELL arrow from the sell trend', fakeAsync(() => {
    pick('EUR', 'USD');
    flushRate('EUR', 1.1, 1.2);
    fixture.detectChanges(); // the template reads the trends after every rate, as it does in the app
    tick(2000);
    flushRate('EUR', 1.3, 1.1); // buy UP, sell DOWN
    fixture.detectChanges();

    const [sell, buy] = Array.from<HTMLElement>(
      fixture.nativeElement.querySelectorAll('.rates-container > div')
    );
    expect(sell.querySelector('.rate-down')).not.toBeNull();
    expect(sell.querySelector('.rate-up')).toBeNull();
    expect(buy.querySelector('.rate-up')).not.toBeNull();
    expect(buy.querySelector('.rate-down')).toBeNull();

    fixture.destroy();
  }));

  it('swapping the pair stops polling the old pair and polls the new one', fakeAsync(() => {
    pick('EUR', 'USD');
    flushRate('EUR', 1.1, 1.2);

    component.switchCCY();
    fixture.detectChanges();

    const [request] = rateRequests('USD');
    expect(request.request.params.get('secondaryCcy')).toBe('EUR');
    request.flush({ buyRate: 0.9, sellRate: 0.8, ts: 0 });
    expect(component.buyTrend()).toBe(RateTrend.FLAT);

    tick(2000);
    expect(rateRequests('EUR').length).withContext('old pair polled again').toBe(0);
    expect(rateRequests('USD').length).withContext('new pair polled').toBe(1);

    fixture.destroy();
  }));

  it('saves a transaction with the current rate and a millisecond date', fakeAsync(() => {
    localStorage.setItem('currentUser', JSON.stringify({ username: 'ana' }));
    const before = Date.now();
    pick('EUR', 'USD');
    flushRate('EUR', 1.1, 1.2);
    component.notional.set(1000);
    component.tenor.set('SP');
    expect(component.canTrade()).toBeTrue();

    component.onBuy();
    const save: TestRequest = http.expectOne(
      (r) => r.method === 'POST' && r.url === backendUrl.fxTradeService.saveTransaction
    );
    expect(save.request.body).toEqual(
      jasmine.objectContaining({
        username: 'ana',
        primaryCcy: 'EUR',
        secondaryCcy: 'USD',
        rate: 1.1,
        action: 'BUY',
        notional: 1000,
        tenor: 'SP',
      })
    );
    expect(save.request.body.date).toBeGreaterThanOrEqual(before);
    expect(save.request.body.date).toBeLessThanOrEqual(Date.now());
    save.flush({});

    localStorage.removeItem('currentUser');
    fixture.destroy();
  }));

  it('shows an error toast and sends nothing when amount or tenor is missing', fakeAsync(() => {
    const error = spyOn(TestBed.inject(ToastrService), 'error');
    pick('EUR', 'USD');
    flushRate('EUR', 1.1, 1.2);

    expect(component.canTrade()).toBeFalse();
    component.onSell();

    expect(error).toHaveBeenCalledWith('Please enter a positive Amount and select a Tenor!');
    http.expectNone((r) => r.method === 'POST');
    fixture.destroy();
  }));
  it('rejects zero, negative and non-finite amounts without sending a trade', () => {
    spyOn(TestBed.inject(ToastrService), 'error');
    component.tenor.set('SP');
    for (const amount of [0, -100, NaN, Infinity]) {
      component.notional.set(amount);
      expect(component.canTrade()).toBeFalse();
      component.onBuy();
    }
    http.expectNone((r) => r.method === 'POST');
  });

});
