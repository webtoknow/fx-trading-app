import { Component, OnInit, Input, Output, EventEmitter, OnDestroy } from '@angular/core';
import { Widget } from 'src/app/models/widget';
import { TradeService } from 'src/app/services/trade.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-widget',
  templateUrl: './widget.component.html',
  styleUrls: ['./widget.component.css']
})
export class WidgetComponent implements OnInit, OnDestroy {
  tenors = ['SP', '1M', '3M'];
  unsubscribe = new Subject();
  buyRateTrend: string;
  sellRateTrend: string;
  
  @Input() widget: Widget;
  @Input() index: number;
  @Input() currencies: string[];
  @Output() deleted = new EventEmitter<number>();

  constructor(
    private tradeService: TradeService
  ) { }

  ngOnInit() {
  }

  onDelete() {
    this.deleted.emit(this.index);
  }

  onSell() {
    const username: string  = JSON.parse(localStorage.getItem('currentUser')).username;
    this.tradeService.saveTransaction({
      username: username,
      primaryCCY: this.widget.primaryCCY,
      secondaryCCY: this.widget.secondaryCCY,
      rate: this.widget.sellRate,
      action: 'sell',
      notional: this.widget.notional,
      tenor: this.widget.tenor,
      date: Math.round(new Date().getTime()/1000)
    }).subscribe(response => {
      console.log('Transaction saved', response)
    })
  }
  onBuy() {
    const username: string  = JSON.parse(localStorage.getItem('currentUser')).username;
    this.tradeService.saveTransaction({
      username: username,
      primaryCCY: this.widget.primaryCCY,
      secondaryCCY: this.widget.secondaryCCY,
      rate: this.widget.buyRate,
      action: 'buy',
      notional: this.widget.notional,
      tenor: this.widget.tenor,
      date: Math.round(new Date().getTime()/1000)
    }).subscribe(response => {
      console.log('Transaction saved', response)
    })
  }

  onCCYChange() {
    this.switchCCY()
  }
  
  switchCCY() {
    const tempCCY = this.widget.primaryCCY;
    this.widget.primaryCCY = this.widget.secondaryCCY;
    this.widget.secondaryCCY= tempCCY;
  }

  startPooling() {
    const { primaryCCY, secondaryCCY } = this.widget;
    this.tradeService.getFxRatePolling(primaryCCY, secondaryCCY)
      .pipe(takeUntil(this.unsubscribe))
      .subscribe((response) => {

        this.buyRateTrend = this.widget.buyRate > response.buyRate ? 'down' : 'up'
        this.sellRateTrend = this.widget.sellRate > response.sellRate ? 'down' : 'up'

        this.widget.buyRate = response.buyRate;
        this.widget.sellRate = response.sellRate;
      });
  }

  onPickCurrency() {
    const { primaryCCY, secondaryCCY } = this.widget;
    if (primaryCCY && secondaryCCY && primaryCCY !== secondaryCCY) {
      this.widget.pickCCYState = false;
      this.startPooling();
    }
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
    }
}
