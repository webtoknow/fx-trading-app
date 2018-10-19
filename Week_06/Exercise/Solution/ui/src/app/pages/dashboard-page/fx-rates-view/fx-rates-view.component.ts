import { Component, OnInit } from '@angular/core';
import { Widget } from 'src/app/models/widget';
import { TradeService } from 'src/app/services/trade.service';

@Component({
  selector: 'app-fx-rates-view',
  templateUrl: './fx-rates-view.component.html',
  styleUrls: ['./fx-rates-view.component.css']
})
export class FxRatesViewComponent implements OnInit {
  widgets: Widget[] = [];
  currencies: string[] = [];

  constructor(
    private tradeService: TradeService
  ) { }

  ngOnInit() {
    this.tradeService.getCurrencies().subscribe((response) => {
      this.currencies = response;
    })
  }

  onAddWidget() {
    this.widgets = [...this.widgets, new Widget('', '', 0, 0, null, '', true)]
  }
  
  onDeleteWidget(index: number) {
    this.widgets.splice(index, 1);
  }
}
