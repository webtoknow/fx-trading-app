import { Component, OnInit } from '@angular/core';
import { Transaction, SortType } from 'src/app/models/transaction';
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
    ccyPair: '',
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
        // Create transaction transform list by adding ccyPair
        const transactionsWithCcyPair: Transaction[] = response
          .map(transaction => ({ ...transaction, ccyPair: `${transaction.primaryCcy}/${transaction.secondaryCcy}`}))
        this.transactions = transactionsWithCcyPair;
        this.initialTransactions = [...transactionsWithCcyPair];
        // Get all Ccy pairs for select
        this.currenciesPairs = this.transactions
          .map(transaction => transaction.ccyPair)
          .filter((x, i, a) => x && a.indexOf(x) === i);
        this.filterBy();
        this.sortBy();
      });
  }

  onSort(column: string) {
    this.sorter.column = column;
    if (this.sorter.type === SortType.DEFAULT) {
      this.sorter.type = SortType.ASC;
    }
    else if (this.sorter.type === SortType.ASC) {
      this.sorter.type = SortType.DESC;
    }
    else {
      this.sorter.type = SortType.DEFAULT;
    }

    this.sortBy();
  }

  sortBy(): void {
    const { column, type } = this.sorter;

    if (type === SortType.DEFAULT) {
      this.transactions = [...this.initialTransactions];
      return
    }
    
    if (type === SortType.ASC) {
      this.transactions.sort((a: any, b: any) => 0 - (a[column] > b[column] ? -1 : 1));
      return;
    }

    if (type === SortType.DESC) {
      this.transactions.sort((a: any, b: any) => 0 - (a[column] > b[column] ? 1 : -1));
      return;
    }
 
  }

  getDateWithoutHourAndMinuteAndSeconds(date) {
    return new Date(new Date(date).getFullYear(), new Date(date).getMonth(), new Date(date).getDay());
  }

  filterBy(): void {
    this.transactions = this.initialTransactions
      .filter(transaction =>
        this.filter.ccyPair && transaction.ccyPair === this.filter.ccyPair || !this.filter.ccyPair)
      .filter(transaction =>
        this.filter.date && this.getDateWithoutHourAndMinuteAndSeconds(transaction.date).getTime() === this.getDateWithoutHourAndMinuteAndSeconds(this.filter.date).getTime() || !this.filter.date)
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
    }
}
