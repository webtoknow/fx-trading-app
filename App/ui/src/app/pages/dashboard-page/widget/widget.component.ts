import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Widget } from 'src/app/models/widget';

@Component({
  selector: 'app-widget',
  templateUrl: './widget.component.html',
  styleUrls: ['./widget.component.css']
})
export class WidgetComponent implements OnInit {
  currencies = ['USD', 'EUR', 'GBP', 'JPY', 'RON'];
  tenors = ['Spot', '1M', '3M'];

  @Input() widget: Widget;
  @Input() index: number;
  @Output() deleted = new EventEmitter<number>();

  constructor() { }
  ngOnInit() {
  }

  onDelete() {
    this.deleted.emit(this.index);
  }

  onCCYChange() {
    
  }

  onSelectCurrency() {
    if (this.widget.primaryCCY && this.widget.secondaryCCY) {
      this.widget.pickCCYState = false;
    }
  }
}
