import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Widget } from 'src/app/models/widget';

@Component({
  selector: 'app-widget',
  templateUrl: './widget.component.html',
  styleUrls: ['./widget.component.css']
})
export class WidgetComponent implements OnInit {
  tenors = ['Spot', '1M', '3M'];

  @Input() widget: Widget;
  @Input() index: number;
  @Input() currencies: string[];
  @Output() deleted = new EventEmitter<number>();

  constructor() { }
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

  onSelectCurrency() {
    const { primaryCCY, secondaryCCY } = this.widget;
    if (primaryCCY && secondaryCCY && primaryCCY !== secondaryCCY) {
      this.widget.pickCCYState = false;
    }
  }
}
