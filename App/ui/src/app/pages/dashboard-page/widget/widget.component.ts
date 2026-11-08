import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
  linkedSignal,
  output,
} from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { EMPTY, of } from 'rxjs';
import { catchError, startWith, switchMap } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { TradeService } from '../../../services/trade.service';
import { RateTrend, Widget } from '../../../models/widget';
import { Rate } from '../../../models/rate';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

function trendOf(previous?: number, current?: number): RateTrend {
  if (previous === undefined || current === undefined) {
    return RateTrend.FLAT;
  }
  return current > previous
    ? RateTrend.UP
    : current < previous
    ? RateTrend.DOWN
    : RateTrend.FLAT;
}

@Component({
  selector: 'app-widget',
  imports: [FormsModule, DecimalPipe],
  templateUrl: './widget.component.html',
  styleUrls: ['./widget.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WidgetComponent {
  private tradeService = inject(TradeService);
  private toastr = inject(ToastrService);

  tenors = ['SP', '1M', '3M'];
  RateTrend = RateTrend;

  // Inputs and output
  widget = input.required<Widget>();
  index = input.required<number>();
  currencies = input<string[]>([]);
  deleted = output<number>();

  pickCCYState = linkedSignal(() => this.widget().pickCCYState);
  primaryCcy = linkedSignal(() => this.widget().primaryCcy);
  secondaryCcy = linkedSignal(() => this.widget().secondaryCcy);
  notional = linkedSignal(() => this.widget().notional);
  tenor = linkedSignal(() => this.widget().tenor);

  pair = computed(() => {
    const primary = this.primaryCcy();
    const secondary = this.secondaryCcy();
    if (this.pickCCYState() || !primary || !secondary || primary === secondary) {
      return null;
    }
    return { primary, secondary };
  });

  rates = toSignal(
    toObservable(this.pair).pipe(
      switchMap((pair) =>
        pair
          ? this.tradeService
              .getFxRatePolling(pair.primary, pair.secondary)
              .pipe(
                startWith(null),
                catchError(() => {
                  this.toastr.error('Could not load the rates!');
                  return EMPTY;
                })
              )
          : of(null)
      )
    )
  );

  buyTrend = linkedSignal<Rate | null | undefined, RateTrend>({
    source: this.rates,
    computation: (rate, previous) =>
      trendOf(previous?.source?.buyRate, rate?.buyRate),
  });
  sellTrend = linkedSignal<Rate | null | undefined, RateTrend>({
    source: this.rates,
    computation: (rate, previous) =>
      trendOf(previous?.source?.sellRate, rate?.sellRate),
  });

  canTrade = computed(() => !!this.notional() && !!this.tenor());

  onDelete() {
    this.deleted.emit(this.index());
  }

  onSell() {
    this.trade('SELL', (rate) => rate.sellRate);
  }

  onBuy() {
    this.trade('BUY', (rate) => rate.buyRate);
  }

  private trade(action: 'BUY' | 'SELL', pick: (rate: Rate) => number) {
    const pair = this.pair();
    const rate = this.rates();
    if (!this.canTrade()) {
      this.toastr.error('Please fill in both Amount and Tenor!');
      return;
    }
    if (!pair || !rate) {
      this.toastr.error('No rate available yet!');
      return;
    }
    this.tradeService
      .saveTransaction({
        username: this.currentUsername(),
        primaryCcy: pair.primary,
        secondaryCcy: pair.secondary,
        rate: pick(rate),
        action,
        notional: this.notional(),
        tenor: this.tenor(),
        date: Date.now(),
      })
      .subscribe({
        next: () => this.toastr.success('Transaction saved!'),
        error: () => this.toastr.error('Transaction could not be saved!'),
      });
  }

  private currentUsername(): string {
    try {
      return JSON.parse(localStorage.getItem('currentUser') ?? 'null')?.username ?? '';
    } catch {
      return '';
    }
  }

  onCCYChange() {
    this.switchCCY();
  }

  switchCCY() {
    const previousPrimary = this.primaryCcy();
    this.primaryCcy.set(this.secondaryCcy());
    this.secondaryCcy.set(previousPrimary);
  }

  onPickCurrency() {
    const primary = this.primaryCcy();
    const secondary = this.secondaryCcy();
    if (primary && secondary && primary !== secondary) {
      this.pickCCYState.set(false);
    } else if (!primary || !secondary) {
      this.toastr.error('Please select both Primary and Secondary Currencies!');
    } else {
      this.toastr.error(
        'Please select different Primary and Secondary Currencies!'
      );
    }
  }
}
