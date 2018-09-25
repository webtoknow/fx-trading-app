import { Component, OnInit } from '@angular/core';
import { Transaction } from 'src/app/models/transaction';
import { filter } from 'rxjs/internal/operators/filter';
import { TradeService } from '../../../services/trade.service';
import { Subject } from 'rxjs';
import { takeUntil, map } from 'rxjs/operators';


@Component({
  selector: 'app-blotter-view',
  templateUrl: './blotter-view.component.html',
  styleUrls: ['./blotter-view.component.css']
})
export class BlotterViewComponent implements OnInit {

  private sorted = false;
  private filter = {
    CCYPair: '',
    date: ''
  };
  unsubscribe = new Subject();
  transactions: Transaction[] = [];
  initialTransactions: Transaction[] = [];
  currenciesPairs: string[] = [];

  constructor(
    private tradeService: TradeService
  ) { }

  ngOnInit() {
    this.startPooling();
  }


  startPooling(): void {
    this.tradeService.getTransactionsPolling()
      .pipe(takeUntil(this.unsubscribe))
      .subscribe(response => {
        const transactionsWithCCYPair: Transaction[] = response
          .map(transaction => ({ ...transaction, CCYPair: `${transaction.primaryCCY}/${transaction.secondaryCCY}`}))
        this.transactions = transactionsWithCCYPair;
        this.initialTransactions = [...transactionsWithCCYPair];
        this.currenciesPairs = this.transactions
          .map(transaction => transaction.CCYPair)
          .filter((x, i, a) => x && a.indexOf(x) === i)
      });
  }

  sortBy(sortCriteria: any): void {
    this.transactions.sort((a: any, b: any) => {
      if (a[sortCriteria] < b[sortCriteria]) {
        return this.sorted ? 1 : -1;
      }
      if (a[sortCriteria] > b[sortCriteria]) {
        return this.sorted ? -1 : 1;
      }
      return 0;
    })
    this.sorted = !this.sorted;
  }

  getDateWithoutHourAndMinuteAndSeconds(date) {
    return new Date(new Date(date).getFullYear(), new Date(date).getMonth(), new Date(date).getDay());
  }

  filterBy(filterCriteria: any): void {
    this.transactions = this.initialTransactions
      .filter(transaction =>
        this.filter.CCYPair && transaction.CCYPair === this.filter.CCYPair || !this.filter.CCYPair)
      .filter(transaction =>
        this.filter.date && this.getDateWithoutHourAndMinuteAndSeconds(transaction.date).getTime() === this.getDateWithoutHourAndMinuteAndSeconds(this.filter.date).getTime() || !this.filter.date)
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
    }
}
