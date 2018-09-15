import { Component, OnInit } from '@angular/core';
import { Widget } from 'src/app/models/widget';

@Component({
  selector: 'app-fx-rates-view',
  templateUrl: './fx-rates-view.component.html',
  styleUrls: ['./fx-rates-view.component.css']
})
export class FxRatesViewComponent implements OnInit {
  widgets: Widget[] = [];

  constructor() { }

  ngOnInit() {
  }

  onAddWidget() {
    this.widgets = [...this.widgets, new Widget('', '', 0, 0, '', '', true)]
  }
  
  onDeleteWidget(index: number) {
    this.widgets.splice(index, 1);
  }
}
