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
    const { notional, tenor } = this.widget;
    if (notional && tenor) {
      const username: string  = JSON.parse(localStorage.getItem('currentUser')).username;
      this.tradeService.saveTransaction({
      username: username,
      primaryCcy: this.widget.primaryCcy,
      secondaryCcy: this.widget.secondaryCcy,
      rate: this.widget.sellRate,
      action: 'sell',
      notional: this.widget.notional,
      tenor: this.widget.tenor,
      date: Math.round(new Date().getTime()/1000)
      }).subscribe(response => {
        console.log('Transaction saved', response)
      })
    } 
  }
  
  onBuy() {
    const { notional, tenor } = this.widget;
    if (notional && tenor) {
      const username: string  = JSON.parse(localStorage.getItem('currentUser')).username;
      this.tradeService.saveTransaction({
      username: username,
      primaryCcy: this.widget.primaryCcy,
      secondaryCcy: this.widget.secondaryCcy,
      rate: this.widget.buyRate,
      action: 'buy',
      notional: this.widget.notional,
      tenor: this.widget.tenor,
      date: Math.round(new Date().getTime()/1000)
      }).subscribe(response => {
        console.log('Transaction saved', response)
      })
    }
  }

  onCCYChange() {
    this.switchCCY()
  }
  
  switchCCY() {
    const tempCCY = this.widget.primaryCcy;
    this.widget.primaryCcy = this.widget.secondaryCcy;
    this.widget.secondaryCcy= tempCCY;
  }

  startPooling() {
    const { primaryCcy, secondaryCcy } = this.widget;
    this.tradeService.getFxRatePolling(primaryCcy, secondaryCcy)
      .pipe(takeUntil(this.unsubscribe))
      .subscribe((response) => {

        this.buyRateTrend = this.widget.buyRate > response.buyRate ? 'down' : 'up'
        this.sellRateTrend = this.widget.sellRate > response.sellRate ? 'down' : 'up'

        this.widget.buyRate = response.buyRate;
        this.widget.sellRate = response.sellRate;
      });
  }

  onPickCurrency() {
    const { primaryCcy, secondaryCcy } = this.widget;
    if (primaryCcy && secondaryCcy && primaryCcy !== secondaryCcy) {
      this.widget.pickCCYState = false;
      this.startPooling();
    }
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
    }
}
