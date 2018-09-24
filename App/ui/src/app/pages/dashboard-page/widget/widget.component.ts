import { Component, OnInit, Input, Output, EventEmitter, OnDestroy } from '@angular/core';
import { Widget } from 'src/app/models/widget';
import { TradeService } from 'src/app/services/trade.service';

@Component({
  selector: 'app-widget',
  templateUrl: './widget.component.html',
  styleUrls: ['./widget.component.css']
})
export class WidgetComponent implements OnInit, OnDestroy {
  tenors = ['Spot', '1M', '3M'];

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
    this.tradeService.getFxRatePolling(primaryCCY, secondaryCCY).subscribe((response) => {
      console.log(response)
    })
  }

  onPickCurrency() {
    const { primaryCCY, secondaryCCY } = this.widget;
    if (primaryCCY && secondaryCCY && primaryCCY !== secondaryCCY) {
      this.widget.pickCCYState = false;
      this.startPooling();
    }
  }

  ngOnDestroy() {
    // this.tradeService.getFxRatePolling('asd', 'asd').unsubscribe();
    }
}
