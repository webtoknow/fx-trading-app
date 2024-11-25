import { Component } from '@angular/core';
import { Transaction } from '../../../models/transaction';
import { Subject, takeUntil } from 'rxjs';
import { TradeService } from '../../../services/trade.service';

@Component({
  selector: 'app-blotter-view',
  templateUrl: './blotter-view.component.html',
  styleUrl: './blotter-view.component.css',
})
export class BlotterViewComponent {
  filter = {
    ccyPair: '',
    date: 0,
  };

  private unsubscribe = new Subject();
  transactions: Transaction[] = [];
  private initialTransactions: Transaction[] = [];

  currenciesPairs: (string | undefined)[] = [];

  constructor(private tradeService: TradeService) {}

  ngOnInit() {
    this.startPooling();
  }

  startPooling(): void {
    this.tradeService
      .getTransactionsPolling()
      .pipe(takeUntil(this.unsubscribe))
      .subscribe((response) => {
        // Create transaction transform list by adding ccyPair
        const transactionsWithCcyPair: Transaction[] = response.map(
          (transaction) => ({
            ...transaction,
            ccyPair: `${transaction.primaryCcy}/${transaction.secondaryCcy}`,
          })
        );
        this.transactions = transactionsWithCcyPair;
        this.initialTransactions = [...transactionsWithCcyPair];
        // Get all Ccy pairs for select
        this.currenciesPairs = this.transactions
          .map((transaction) => transaction.ccyPair)
          .filter((x, i, a) => x && a.indexOf(x) === i);
        this.filterBy();
      });
  }

  getDateWithoutHourAndMinuteAndSeconds(date: number) {
    return new Date(
      new Date(date).getFullYear(),
      new Date(date).getMonth(),
      new Date(date).getDay()
    );
  }

  filterBy(): void {
    this.transactions = this.initialTransactions
      .filter(
        (transaction) =>
          (this.filter.ccyPair &&
            transaction.ccyPair === this.filter.ccyPair) ||
          !this.filter.ccyPair
      )
      .filter(
        (transaction) =>
          (this.filter.date &&
            this.getDateWithoutHourAndMinuteAndSeconds(
              transaction.date
            ).getTime() ===
              this.getDateWithoutHourAndMinuteAndSeconds(
                this.filter.date
              ).getTime()) ||
          !this.filter.date
      );
  }

  ngOnDestroy() {
    this.unsubscribe.next('');
    this.unsubscribe.complete();
  }
}
