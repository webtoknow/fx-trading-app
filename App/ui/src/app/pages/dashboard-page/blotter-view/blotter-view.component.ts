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

  private filter = {
    CcyPair: '',
    date: ''
  };
  private sorter = {
    column: '',
    type: ''
  }
  private unsubscribe = new Subject();
  private transactions: Transaction[] = [];
  private initialTransactions: Transaction[] = [];
  private currenciesPairs: string[] = [];

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
        // Create transaction transform list by adding CcyPair
        const transactionsWithCcyPair: Transaction[] = response
          .map(transaction => ({ ...transaction, CcyPair: `${transaction.primaryCcy}/${transaction.secondaryCcy}`}))
        this.transactions = transactionsWithCcyPair;
        this.initialTransactions = [...transactionsWithCcyPair];
        // Get all Ccy pairs for select
        this.currenciesPairs = this.transactions
          .map(transaction => transaction.CcyPair)
          .filter((x, i, a) => x && a.indexOf(x) === i);
        this.filterBy();
        this.sortBy();
      });
  }

  onSort(column: string) {
    this.sorter.column = column;
    if (this.sorter.type) {
      if (this.sorter.type === 'asc') {
        this.sorter.type='dec'
      } else {
        this.sorter.type=''
      }
    } else {
      this.sorter.type='asc'
    }
    this.sortBy()
  }

  sortBy(): void {
    const { column, type } = this.sorter;

    if (type === '') {
      this.transactions = this.initialTransactions
      return
    }
 
    this.transactions.sort((a: any, b: any) => {
      if (a[column] < b[column]) {
        return type === 'asc' ? 1 : -1;
      }
      if (a[column] > b[column]) {
        return type === 'des' ? -1 : 1;
      }
      return 0;
    })
  }

  getDateWithoutHourAndMinuteAndSeconds(date) {
    return new Date(new Date(date).getFullYear(), new Date(date).getMonth(), new Date(date).getDay());
  }

  filterBy(): void {
    this.transactions = this.initialTransactions
      .filter(transaction =>
        this.filter.CcyPair && transaction.CcyPair === this.filter.CcyPair || !this.filter.CcyPair)
      .filter(transaction =>
        this.filter.date && this.getDateWithoutHourAndMinuteAndSeconds(transaction.date).getTime() === this.getDateWithoutHourAndMinuteAndSeconds(this.filter.date).getTime() || !this.filter.date)
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
    }
}
