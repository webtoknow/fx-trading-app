# Week 6 - Create Dashboard page with Angular

## Table of contents

## Exercise 1 - Create blotter-view, fx-rates-view and widget components

Go to *Week_06/Exercise/Code/ui*

```bash
cd fx-trading-app\Week_06\Exercise\Code\ui
```

Run *npm install* to download all dependencies

```bash
npm install
```

By taking a look at the design mockup, we can see that it can be divided in 2 big sections: **FX Rates View** and **Blotter View**. Also, the first one contains many widgets looking the same, so this can be also splitted into **Widget** components.

So, *dashboard-page* component will use 3 smaller components:

- *blotter-view*
- *fx-rates-view*
- *widget*

Let's create them!

First, go to *Week_06/Exercise/Code/ui/src/app/pages/dashboard-page* and use Angular CLI to create these 3 new components, children of *dashboard-page* components:

```bash
ng generate component blotter-view
ng generate component fx-rates-view
ng generate component widget
```

Also, by using Angular CLI, we can see that *app.module.ts* was also updated to contain the new components.

In the design, we can see on the top a navbar, containing the logo and *Logout* button.
Let's include this and also *blotter-view* and *fx-rates-view* into *dashboard-page* component and use *bootstrap* to place them.

So, in *dashboard-page.component.html*, we will have:

```HTML
<!-- Navigation -->
<header>
  <nav class="navbar">
    <img class="fx-main-logo" alt="fx-main-logo" src="./assets/img/logo-main.svg">
    <a href="#" class="btn btn-logout">Log out</a>
  </nav>
</header>

<div class="dashboard-container">
  <div class="row">
    <div class="col-sm">
      <app-fx-rates-view></app-fx-rates-view>
    </div>
    <div class="col-sm">
      <app-blotter-view></app-blotter-view>
    </div>
  </div>
</div>
```

and in *dashboard-page.component.css*:

```CSS
.dashboard-container {
  padding: 2rem 3rem;
}

.navbar {
    padding: 0.5rem 3rem;
    border: 1px solid #DDDDDD;
}

.fx-main-logo {
    width: 70px;
    height: 50px;
}

.btn-logout {
    border: 1px solid #dddddd;
    color: #7c7c7c;
}

.btn-logout:hover {
    background-color: #F2F2F2;
    opacity: 0.8;
}
```

## Exercise 2 - Install and use JSON Server

Because we do not have a backend server and a link to a real database at this moment, we will simulate having some data using JSON Server.

The first step is to install it (globally), using the following command:

```bash
npm install json-server -g
```

For the next step, we have just to start it with the 2 existing files - containing *quote* and *trade* data. Make sure you are in the following path, where both JSON files are situated: *Week_06/Exercise/Code/ui*. Please run these commands in separate terminal windows:

```bash
json-server --watch db.trade.json --port 8210
json-server --watch db.quote.json --port 8220
```

## Exercise 3 - Blotter View page

### Transaction model

Into *Week_06/Exercise/Code/ui/src/app/models*, let's make a new file, named *transaction.ts*,representing the *Transaction* model:

```JavaScript
export interface Transaction {
  id?: number;
  username: string;
  primaryCcy: string
  secondaryCcy: string;
  rate: number;
  action: string;
  notional: number;
  tenor: string;
  date: number;
  ccyPair?: string
}
```

As we can see in the design, some columns of the table filled in with transactions will be sortable, so let's add also an enum here containing the type of sort:

```JavaScript
export enum SortType {
  ASC = 'asc',
  DESC = 'desc',
  DEFAULT = ''
}
```

### Trade service

In *Week_06/Exercise/Code/ui/src/app/services*, let's create a file, *trade.service.ts*, which will contain all API calls desired to get data from JSON Server.

Let's add our first method here, which will get all transactions for us, to be displayed in the table from the right side of the page:

```JavaScript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Transaction } from 'src/app/models/transaction';
import { Observable } from 'rxjs/internal/Observable';

import { backendUrl } from '../constants';

@Injectable()
export class TradeService {

    constructor(
        private http: HttpClient
    ) { }

    getTransactions() {
        return this.http.get(backendUrl.fxTradeService.getTransactions) as Observable<Transaction[]>
    }
}

```

So, *getTransactions()* method will call the API defined in constants (*backendUrl.fxTradeService.getTransactions*) through HTTP.

### Update Application Module

In app.module.ts, include *Trade Service*:

```JavaScript
import { TradeService } from './services/trade.service';

providers: [
    ...,
    TradeService,
    ...
]
```

### Implement pooling mechanism

We want to simulate the real-time behavior for getting the transactions. THis is the reason why we implement pooling mechanish.

So, we should add a new method in *trade.service.ts*, which will make a call to get all transactions every 2 seconds:

```JavaScript
import { interval } from "rxjs";
import {startWith, switchMap} from "rxjs/operators";

  getTransactionsPolling() {
        return interval(2000)
            .pipe(
                startWith(0),
                switchMap(() => this.http.get(backendUrl.fxTradeService.getTransactions)
            )
        ) as Observable<Transaction[]>
    }
```

### Blotter View component

- **blotter-view.component.html**:

```HTML
<div class="title title-border">
  <h4>Blotter View</h4>
</div>
<div class="filter-container">
  <span class="flex-vertical-centered filter-label">Filters</span>
  <span class="flex-vertical-centered filter-separator">|</span>
  <div class="filter-input-container">
    <div class="flex-vertical-centered filter-group">
      <span>Ccy&nbsp;Pair&nbsp;&nbsp;</span>
      <select name="Ccy" id="Ccy" class="form-control form-control-sm" [(ngModel)]="filter.ccyPair" (ngModelChange)="filterBy($event)">
        <option value="" selected>Please select&nbsp;&nbsp;</option>
        <option *ngFor="let currencyPair of currenciesPairs" [value]="currencyPair">{{ currencyPair }}</option>
      </select>
    </div>

    <div class="flex-vertical-centered filter-group">
      <span for="dateFilter">Date&nbsp;&nbsp;</span>
      <div class="input-group input-group-sm">
        <input 
          type="text" 
          class="form-control form-control-sm" 
          placeholder="Please select&nbsp;" 
          [(ngModel)]="filter.date" 
          (ngModelChange)="filterBy($event)" 
          [bsConfig]="{ dateInputFormat: 'DD/MM/YYYY' }"
          bsDatepicker>
        <div class="input-group-append">
          <span class="input-group-text calendar-icon" id="date-picker-icon">
            <i class="fa fa-calendar-alt icon" aria-hidden="true"></i>
          </span>
        </div>
      </div>
    </div>
  </div>
</div>

<div class="table-responsive">
  <table class="table table-striped table-sm">
    <thead class="blotter-table-header">
      <tr>
        <th>ID</th>
        <th class="clickable" (click)="onSort('username')">
          <span>Username&nbsp;</span>
          <i class="fas fa-sort"></i>
        </th>
        <th class="clickable" (click)="onSort('ccyPair')">
          <span>Ccy Pair&nbsp;</span>
          <i class="fas fa-sort"></i>
        </th>
        <th>Rate</th>
        <th class="clickable" (click)="onSort('action')">
          <span>Action&nbsp;</span>
          <i class="fas fa-sort"></i>
        </th>
        <th class="clickable" (click)="onSort('notional')">
          <span>Notional&nbsp;</span>
          <i class="fas fa-sort"></i>
        </th>
        <th>Tenor</th>
        <th class="clickable" (click)="onSort('date')">
          <span>Transaction Date&nbsp;</span>
          <i class="fas fa-sort"></i>
        </th>
      </tr>
    </thead>
    <tbody>
      <tr *ngFor="let transaction of transactions">
        <td>{{ transaction.id }}</td>
        <td>{{ transaction.username }}</td>
        <td>{{ transaction.ccyPair }}</td>
        <td>{{ transaction.rate | number }}</td>
        <td>{{ transaction.action }}</td>
        <td>{{ transaction.notional | number }}</td>
        <td>{{ transaction.tenor }}</td>
        <td>{{ transaction.date | date:'dd/MM/yyyy HH:mm' }}</td>
      </tr>
    </tbody>
  </table>
</div>
```

In this file, we display a table containing the following information from all transactions got from the backend:

- id
- username
- ccyPair
- rate
- action
- notional
- tenor
- date

The user will be able to sort this table by *Username*, *CCY Pair*, *Action*, *Notional* and *Date* and to filter by *CCY Pair* and *Date*.

- **blotter-view.component.css**:

```CSS
.filter-container {
    margin-bottom: 22px;
    display: flex;
}

.filter-label {
    text-transform: uppercase;
    font-weight: bold;
}

.filter-separator {
    color: rgb(221,221,221);
    margin-left: 10px;
    margin-right: 10px;
}

.filter-input-container {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    flex: 1;
}

.filter-group {
   margin-right: 15px;
}

.calendar-icon {
    background-color: white;
    color: #7C7C7C;
}

.calendar-icon > i {
    font-size: 18px;
}

.blotter-table-header {
    background: #3496F0;
    color: white;
    overflow: hidden;
    white-space: nowrap;
}

.blotter-table-header th {
    padding-bottom: 6px;
    padding-top: 6px;
}
```

- **blotter-view.component.ts**:

```JavaScript
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

```

So, in this file:

- when we initiate this (*ngOnInit*), we should call *startPooling* method, which calls *tradeService.getTransactionsPolling()*
- after getting all transactions, *startPooling* method:
  - adds *ccyPair* property to all transactions by concatenating *primaryCCY* and *secondaryCCY*
  - gets all *currenciesPairs* to fill in the filter select
  - applies default sort and filter (with no criteria)
- *onSort* method sets the sorting type:
  - switches DEFAULT to ASC on first click
  - switches ASC to DESC on second click
  - switches DESC to DEFAULT on third click
- *sortBy* method sorts by column and sorting type
- *filterBy* method does the filtering functionality - by ccyPair and/or date
- *ngOnDestroy* unsubscribes from getting transactions