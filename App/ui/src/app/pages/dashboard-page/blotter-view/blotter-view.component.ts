import { Component, OnInit } from '@angular/core';
import { Transaction } from 'src/app/models/transaction';
import { filter } from 'rxjs/internal/operators/filter';
import { BlotterService } from 'src/app/services/blotter.service';


@Component({
  selector: 'app-blotter-view',
  templateUrl: './blotter-view.component.html',
  styleUrls: ['./blotter-view.component.css']
})
export class BlotterViewComponent implements OnInit {

  private sorted = false;
  private filter = {
    ccy: '',
    date: ''
  };

  transactions: Transaction[] = [
    {
      dealId: 1234,
      username: "Catalin Popescu",
      CCY: "USD/EUR",
      rate: "0.86",
      action: "SELL",
      notional: 1000000,
      tenor: "1M",
      date: 1537511800526
    },
    {
      dealId: 212,
      username: "Bogdan Pascu",
      CCY: "USD/EUR",
      rate: "0.15",
      action: "BUY",
      notional: 1000000,
      tenor: "1M",
      date: 1536560103000
    },
    {
      dealId: 34,
      username: "George Popescu",
      CCY: "EUR/USD",
      rate: "0.63",
      action: "SELL",
      notional: 1000000,
      tenor: "3M",
      date: 1537511800526
    },
    {
      dealId: 455,
      username: "Andrei Nare",
      CCY: "USD/EUR",
      rate: "1.11",
      action: "SELL",
      notional: 1000000,
      tenor: "1M",
      date: 1537511800526
    },
    {
      dealId: 55,
      username: "George Barbu",
      CCY: "RON/EUR",
      rate: "0.46",
      action: "SELL",
      notional: 1000000,
      tenor: "1M",
      date: 1536560103000
    },
    {
      dealId: 3133,
      username: "Maria Munteanu",
      CCY: "USD/GBP",
      rate: "1.04",
      action: "BUY",
      notional: 1000000,
      tenor: "1M",
      date: 1537511800526
    },
    {
      dealId: 3123,
      username: "Mihai Nedelcu",
      CCY: "EUR/GBP",
      rate: "0.86",
      action: "BUY",
      notional: 1000000,
      tenor: "1M",
      date: 1536560103000
    },
    {
      dealId: 4523,
      username: "Dan Purcarete",
      CCY: "USD/GBP",
      rate: "0.31",
      action: "SELL",
      notional: 1000000,
      tenor: "1M",
      date: 1537511800526
    },
    {
      dealId: 123,
      username: "Ileana Geo",
      CCY: "RON/USD",
      rate: "0.67",
      action: "SELL",
      notional: 1000000,
      tenor: "3M",
      date: 1536560103000
    },
    {
      dealId: 12,
      username: "Dana Popescu",
      CCY: "EUR/USD",
      rate: "0.61",
      action: "SELL",
      notional: 1000000,
      tenor: "1M",
      date: 1537511800526
    },
    {
      dealId: 1,
      username: "George Nicolae",
      CCY: "USD/GBP",
      rate: "1.23",
      action: "SELL",
      notional: 234324,
      tenor: "1M",
      date: 1537511800526
    },
    {
      dealId: 1234,
      username: "Piti Popescu",
      CCY: "USD/RON",
      rate: "1.01",
      action: "BUY",
      notional: 10050000,
      tenor: "1M",
      date: 1536560103000
    },
    {
      dealId: 55,
      username: "Adriana Popescu",
      CCY: "USD/EUR",
      rate: "0.12",
      action: "BUY",
      notional: 1234,
      tenor: "1M",
      date: 1536560103000
    },
    {
      dealId: 5234,
      username: "George Marcu",
      CCY: "USD/RON",
      rate: "0.45",
      action: "BUY",
      notional: 555.23,
      tenor: "3M",
      date: 1536560103000
    },
  ];

   currencies = this.transactions
     .map(transaction => transaction.CCY)
     .filter((x, i, a) => x && a.indexOf(x) === i)

   initialTransactions = [...this.transactions];

  // Uncomment when BE is ready

  /*private transactions: Transaction[]
  private currencies: string[] = [];
  private initialTransactions: Transaction[] = [];*/

  constructor(
    private blotterService: BlotterService
  ) { }

  ngOnInit() {
    // Uncomment when BE is ready
    // this.getTransactions();
  }

  getTransactions(): void {
    this.blotterService.getTransactions()
      .subscribe(transactions => {
        this.transactions = transactions
        this.initialTransactions = [...this.transactions];
        this.currencies = this.transactions
          .map(transaction => transaction.CCY)
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
        this.filter.ccy && transaction.CCY === this.filter.ccy || !this.filter.ccy)
      .filter(transaction =>
        this.filter.date && this.getDateWithoutHourAndMinuteAndSeconds(transaction.date).getTime() === this.getDateWithoutHourAndMinuteAndSeconds(this.filter.date).getTime() || !this.filter.date)
  }

}
