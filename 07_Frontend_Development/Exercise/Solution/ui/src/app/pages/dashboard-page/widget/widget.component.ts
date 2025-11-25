import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
  OnDestroy,
} from '@angular/core';
import { TradeService } from '../../../services/trade.service';
import { RateTrend, Widget } from '../../../models/widget';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-widget',
  templateUrl: './widget.component.html',
  styleUrls: ['./widget.component.css'],
  standalone: false
})
export class WidgetComponent implements OnInit, OnDestroy {
  tenors = ['SP', '1M', '3M'];
  unsubscribe = new Subject();
  buyRateTrend: RateTrend = RateTrend.FLAT;
  sellRateTrend: RateTrend = RateTrend.FLAT;
  public RateTrend = RateTrend;

  @Input() widget: Widget = new Widget();
  @Input() index: number = 0;
  @Input() currencies: string[] = [];
  @Output() deleted = new EventEmitter<number>();

  constructor(
    private tradeService: TradeService,
    private toastr: ToastrService
  ) {}

  ngOnInit() {}

  onDelete() {
    this.deleted.emit(this.index);
  }

  onSell() {
    const { notional, tenor } = this.widget;
    if (notional && tenor) {
      const username: string =
        JSON.parse(localStorage.getItem('currentUser') || '').username || '';
      this.tradeService
        .saveTransaction({
          username: username,
          primaryCcy: this.widget.primaryCcy,
          secondaryCcy: this.widget.secondaryCcy,
          rate: this.widget.sellRate,
          action: 'SELL',
          notional: this.widget.notional,
          tenor: this.widget.tenor,
          date: Date.now(),
        })
        .subscribe((response) => {
          this.toastr.success('Transaction saved!');
        });
    } else {
      this.toastr.error('Please fill in both Amount and Tenor!');
    }
  }

  onBuy() {
    const { notional, tenor } = this.widget;
    if (notional && tenor) {
      const username: string =
        JSON.parse(localStorage.getItem('currentUser') || '').username || '';
      this.tradeService
        .saveTransaction({
          username: username,
          primaryCcy: this.widget.primaryCcy,
          secondaryCcy: this.widget.secondaryCcy,
          rate: this.widget.buyRate,
          action: 'BUY',
          notional: this.widget.notional,
          tenor: this.widget.tenor,
          date: Date.now(),
        })
        .subscribe((response) => {
          this.toastr.success('Transaction saved!');
        });
    } else {
      this.toastr.error('Please fill in both Amount and Tenor!');
    }
  }

  onCCYChange() {
    this.switchCCY();
  }

  switchCCY() {
    const tempCCY = this.widget.primaryCcy;
    this.widget.primaryCcy = this.widget.secondaryCcy;
    this.widget.secondaryCcy = tempCCY;
  }

  startPooling() {
    const { primaryCcy, secondaryCcy } = this.widget;
    this.tradeService
      .getFxRatePolling(primaryCcy, secondaryCcy)
      .pipe(takeUntil(this.unsubscribe))
      .subscribe((response) => {
        this.widget.buyRate = response.buyRate;
        this.widget.sellRate = response.sellRate;

        if (this.widget.buyRate === 0 || this.widget.sellRate === 0) {
          return;
        }

        this.buyRateTrend =
          this.widget.buyRate > response.buyRate
            ? RateTrend.DOWN
            : this.widget.buyRate < response.buyRate
            ? RateTrend.UP
            : RateTrend.FLAT;

        this.sellRateTrend =
          this.widget.sellRate > response.sellRate
            ? RateTrend.DOWN
            : this.widget.sellRate < response.sellRate
            ? RateTrend.UP
            : RateTrend.FLAT;
      });
  }

  onPickCurrency() {
    const { primaryCcy, secondaryCcy } = this.widget;
    if (primaryCcy && secondaryCcy && primaryCcy !== secondaryCcy) {
      this.widget.pickCCYState = false;
      this.startPooling();
    } else if (!primaryCcy || !secondaryCcy) {
      this.toastr.error('Please select both Primary and Secondary Currencies!');
    } else {
      this.toastr.error(
        'Please select different Primary and Secondary Currencies!'
      );
    }
  }

  ngOnDestroy() {
    this.unsubscribe.next('');
    this.unsubscribe.complete();
  }
}
