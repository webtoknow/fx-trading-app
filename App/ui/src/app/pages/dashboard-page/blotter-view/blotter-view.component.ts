import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  signal,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { Transaction } from '../../../models/transaction';
import { TradeService } from '../../../services/trade.service';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';

@Component({
  selector: 'app-blotter-view',
  imports: [FormsModule, BsDatepickerModule, DatePipe, DecimalPipe],
  templateUrl: './blotter-view.component.html',
  styleUrl: './blotter-view.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BlotterViewComponent {
  private tradeService = inject(TradeService);

  transactions = toSignal(this.tradeService.getTransactionsPolling(), {
    initialValue: [] as Transaction[],
  });

  // Filter state
  ccyPair = signal('');
  date = signal<Date | null>(null);

  private withCcyPair = computed<Transaction[]>(() =>
    this.transactions().map((transaction) => ({
      ...transaction,
      ccyPair: `${transaction.primaryCcy}/${transaction.secondaryCcy}`,
    }))
  );

  pairs = computed(() => [
    ...new Set(this.withCcyPair().map((transaction) => transaction.ccyPair)),
  ]);

  rows = computed(() => {
    const ccyPair = this.ccyPair();
    const date = this.date();
    return this.withCcyPair().filter(
      (transaction) =>
        (!ccyPair || transaction.ccyPair === ccyPair) &&
        (!date || this.isSameDay(transaction.date, date))
    );
  });

  // same day = same year, month and getDate() (not getDay())
  private isSameDay(a: number | Date, b: number | Date): boolean {
    const first = new Date(a);
    const second = new Date(b);
    return (
      first.getFullYear() === second.getFullYear() &&
      first.getMonth() === second.getMonth() &&
      first.getDate() === second.getDate()
    );
  }
}
