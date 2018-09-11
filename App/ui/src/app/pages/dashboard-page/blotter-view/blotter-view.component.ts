import { Component, OnInit } from '@angular/core';
import { Transaction } from 'src/app/models/transaction';

@Component({
  selector: 'app-blotter-view',
  templateUrl: './blotter-view.component.html',
  styleUrls: ['./blotter-view.component.css']
})
export class BlotterViewComponent implements OnInit {
  tranzactions: Transaction[] = [
    {
      dealId: 1234,
      username: "Catalin Popescu",
      CCY: "USD",
      notional: "1000000",
      tenor: "1M",
      date: 1536606799000
    },
    {
      dealId: 212,
      username: "Bogdan Pascu",
      CCY: "USD",
      notional: "1000000",
      tenor: "1M",
      date: 1536606771000
    },
    {
      dealId: 34,
      username: "George Popescu",
      CCY: "EUR",
      notional: "1000000",
      tenor: "3M",
      date: 1536605766000
    },
    {
      dealId: 455,
      username: "Andrei Nare",
      CCY: "USD",
      notional: "1000000",
      tenor: "1M",
      date: 1536605766000
    },
    {
      dealId: 55,
      username: "George Barbu",
      CCY: "RON",
      notional: "1000000",
      tenor: "1M",
      date: 1536605766000
    },
    {
      dealId: 3133,
      username: "Maria Munteanu",
      CCY: "USD",
      notional: "1000000",
      tenor: "1M",
      date: 1536605754000
    },
    {
      dealId: 3123,
      username: "Mihai Nedelcu",
      CCY: "EUR",
      notional: "1000000",
      tenor: "1M",
      date: 1536605754000
    },
    {
      dealId: 4523,
      username: "Dan Purcarete",
      CCY: "USD",
      notional: "1000000",
      tenor: "1M",
      date: 1536605754000
    },
    {
      dealId: 123,
      username: "Ileana Geo",
      CCY: "RON",
      notional: "1000000",
      tenor: "3M",
      date: 1536605747000
    },
    {
      dealId: 12,
      username: "Dana Popescu",
      CCY: "EUR",
      notional: "1000000",
      tenor: "1M",
      date: 1536605743000
    },
    {
      dealId: 1,
      username: "George Nicolae",
      CCY: "USD",
      notional: "234324",
      tenor: "1M",
      date: 1536605738000
    },
    {
      dealId: 1234,
      username: "Piti Popescu",
      CCY: "USD",
      notional: "10050000",
      tenor: "1M",
      date: 1536605728000
    },
    {
      dealId: 55,
      username: "Adriana Popescu",
      CCY: "USD",
      notional: "1234",
      tenor: "1M",
      date: 1536605724000
    },
    {
      dealId: 5234,
      username: "George Marcu",
      CCY: "USD",
      notional: "55523",
      tenor: "3M",
      date: 1536604812000
    },
  ]
  constructor() { }
  ngOnInit() {
  }

}
