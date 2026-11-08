import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { Widget } from '../../../models/widget';
import { TradeService } from '../../../services/trade.service';
import { WidgetComponent } from '../widget/widget.component';

@Component({
  selector: 'app-fx-rates-view',
  imports: [WidgetComponent],
  templateUrl: './fx-rates-view.component.html',
  styleUrls: ['./fx-rates-view.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FxRatesViewComponent {
  private tradeService = inject(TradeService);

  currencies = toSignal(this.tradeService.getCurrencies(), {
    initialValue: [] as string[],
  });

  widgets = signal<Widget[]>([]);

  onAddWidget() {
    this.widgets.update((widgets) => [...widgets, new Widget()]);
  }

  onDeleteWidget(index: number) {
    this.widgets.update((widgets) => widgets.filter((_, i) => i !== index));
  }
}
