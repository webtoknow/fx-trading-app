# Week 6 - Create Dashboard page with Angular

## Table of contents

- [Exercise 1 - Create blotter-view, fx-rates-view and widget components](#exercise-1---create-blotter-view-fx-rates-view-and-widget-components)
- [Exercise 2 - Install and use JSON Server](#exercise-2---install-and-use-json-server)
- [Exercise 3 - Blotter View page](#exercise-3---blotter-view-page)
  - [Transaction model](#transaction-model)
  - [Trade service](#trade-service)
  - [Update Application Module](#update-application-module)
  - [Implement pooling mechanism](#implement-pooling-mechanism)
  - [Blotter View component](#blotter-view-component)
- [Exercise 4 - FX Rates View page](#exercise-4---fx-rates-view-page)
  - [Rate model](#rate-model)
  - [Update Trade service](#update-trade-service)
  - [Widget component](#widget-component)
  - [FX Rates View component](#fx-rates-view-component)

## Exercise 1 - Create blotter-view, fx-rates-view and widget components

Go to *Week_06/Exercise/Code/ui*:

```bash
cd fx-trading-app\Week_06\Exercise\Code\ui
```

Run *npm install* to download all dependencies:

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

Because we do not have a backend server and a link to a real database at this moment, we will simulate having some data using *JSON Server*.

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

We want to simulate the real-time behavior for getting the transactions. This is the reason why we implement pooling mechanism.

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
        <td>{{ transaction.action | uppercase }}</td>
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

## Exercise 4 - FX Rates View page

### Rate model

Into *Week_06/Exercise/Code/ui/src/app/models*, let's make a new file, named *rate.ts*, representing the *Rate* model:

```JavaScript
export interface Rate {
  buyRate: number;
  sellRate: number;
  ts: number;
}
```

### Widget model

At same path, *Week_06/Exercise/Code/ui/src/app/models*, we want to have the *widget* class (a new file is required, *widget.ts*):

```JavaScript
export class Widget {
  
  constructor(
    public primaryCcy: string,
    public secondaryCcy: string,
    public buyRate: number,
    public sellRate: number,
    public notional: number,
    public tenor: string,
    public pickCCYState: boolean,
  ) {  }

}
```

### Update Trade service

We need some more methods in *trade.service.ts*:

- one for saving the transaction (if the user press *Sell* or *Buy* buttons):

```JavaScript
 saveTransaction(transaction: Transaction) {
        return this.http.post(backendUrl.fxTradeService.saveTransaction, transaction) as Observable<any>
  }
```

- in order to add a new widget, the user should pick 2 currencies (*Primary* and *Secondary*), then he will be able to view the rates also. For this purpose, 2 new methods will be created:

```JavaScript
  import { Rate } from 'src/app/models/rate';

  getCurrencies() {
        return this.http.get(backendUrl.quoteService.getCurrencies) as Observable<string[]>
  }

  getFxRate(primaryCcy: string, secondaryCcy: string) {
        return this.http.get(backendUrl.quoteService.getFxRate, { params: { primaryCcy, secondaryCcy } }) as Observable<Rate>
  }
```

- as we have simulated a real-time behavior for getting and displaying the transactions, we will do the same for getting FX Rates:

```JavaScript
  getFxRatePolling(primaryCcy: string, secondaryCcy: string) {
        return interval(2000)
            .pipe(
                startWith(0),
                switchMap(() => this.http.get(backendUrl.quoteService.getFxRate, { params: { primaryCcy, secondaryCcy } })
            )
        ) as Observable<Rate>
  }
```

### Widget component

- **widget.component.html**:

```HTML
<div class="content-widget">
  <!-- Close -->
  <span class="fa fa-times close" (click)="onDelete()"></span>

  <!-- Select currency step -->
  <div *ngIf="widget.pickCCYState">
    <h4 class="widget-title">Pick a currency</h4>
    <div class="content-container">
      <div class="form-inline form-inline-long form-group">
          <label class="label-long" for="primaryCcy">Primary</label>
          <select name="primaryCcy" id="primaryCcy" class="form-control" [(ngModel)]="widget.primaryCcy" required>
            <option value="" disabled selected>Please select</option>
            <option *ngFor="let currency of currencies" [value]="currency">{{ currency }}</option>
          </select>
        </div>
        <div class="form-inline form-inline-long form-group">
          <label class="label-long" for="secondaryCcy">Secondary</label>
          <select name="secondaryCcy" id="secondaryCcy" class="form-control" [(ngModel)]="widget.secondaryCcy" required>
            <option value="" disabled selected>Please select</option>
            <option *ngFor="let currency of currencies" [value]="currency">{{ currency }}</option>
          </select>
        </div>
        <div class="btn-wraper">
          <button class="btn btn-primary" (click)="onPickCurrency()">Ok</button>
        </div>
    </div>
  </div>

  <!-- Trade step -->
  <div *ngIf="!widget.pickCCYState">
    <!-- Title -->
    <h4 class="widget-title no-border">
      <span class="widget-primary">{{ widget.primaryCcy }}</span>/{{ widget.secondaryCcy }}
      <span class="fa fa-exchange-alt exchange" (click)="onCCYChange()"></span>
    </h4>
    <!-- Rates -->
    <div class="rates-container">
      <div>
        <span class="widget-subtitle">SELL: </span>
        <span class="rate">{{ widget.sellRate | number:'1.1-2' }}</span>
        <span class='fa' [ngClass]="{'fa-caret-up rate-up': sellRateTrend === 'up', 'fa-caret-down rate-down': sellRateTrend === 'down' }"></span>
      </div>
      <div>
        <span class="widget-subtitle">BUY: </span>
        <span class="rate">{{ widget.buyRate | number:'1.1-2' }}</span>
        <span class="fa" [ngClass]="{'fa-caret-up rate-up': buyRateTrend === 'up', 'fa-caret-down rate-down': buyRateTrend === 'down' }"></span>
      </div>
    </div>
    <div class="content-container">
      <!-- Form  -->
      <div class="form-inline form-group">
        <label class="label-short" for="amount">Amount</label>
        <input type="number" class="form-control" id="amount" placeholder="Type the notional" [(ngModel)]="widget.notional"
          required>
      </div>
      <div class="form-inline form-inline-short form-group">
        <label class="label-short" for="primaryCcy">Tenor</label>
        <select name="tenor" id="tenor" class="form-control" [(ngModel)]="widget.tenor" required>
          <option value="" disabled selected>Please select</option>
          <option *ngFor="let tenor of tenors" [value]="tenor">{{ tenor }}</option>
        </select>
      </div>
      <!-- Buttons  -->
      <div class="btns-wrapper">
        <button class="btn btn-primary" (click)="onSell()">Sell</button>
        <button class="btn btn-success" (click)="onBuy()">Buy</button>
      </div>
    </div>
  </div>
</div>
```

We can notice here:

- a widget can be deleted by pressing the close icon from top-right corner of it
- there are 2 types of widgets:
  - one which allows adding a new currency pair to let the user follow SELL and BUY rates. This contains 2 dropdowns where *Primary* and *Secondary* currencies can be selected - the ones obtained by calling the backend through *getCurrencies()* method from *trade.service.ts*
  - one which allows saving a transaction. For this, the user have to enter the amount he wants to trade, the tenor (*SP* - now, *1M* - in a month or *3M* - in three months) and then press on the button which describes the action he want to do: *Sell* or *Buy*

- **widget.component.css**:

```CSS
.content-widget {
  width: 100%;
  position: relative;
}

.widget-title {
  padding: 1rem 1rem 10px;
  border-bottom: 1px solid #DDDDDD;
  margin: 0;
  font-size: 20px;
  font-weight: bold;
}

.widget-subtitle {
  font-size: 20px;
  color: #7C7C7C;
  font-weight: bold;
}

.widget-primary {
  font-size: 24px;
  color: #373A3C;
}

.widget-primary-currency {
  color: #373A3C;
  font-size: 24px;
}

.label-long {
  width: 80px;
  justify-content: flex-start;
}

.label-short {
  width: 60px;
  justify-content: flex-start;
}

.form-control {
  display: flex;
  flex-grow: 1;
}

.btn-wraper {
  display: grid;
  justify-content: flex-end;
}

.btns-wrapper {
  display: flex;
  justify-content: space-between;
}

.rates-container {
  display: flex;
  justify-content: space-between;
  background: #F2F2F2;
  padding: 10px 1rem;
}

.rate-up {
  color: green;
}

.rate-down {
  color: red;
}

.rate {
  font-size: 30px;
  font-weight: bold;
}

.content-container {
  padding: 1rem;
}

.close {
  position: absolute;
  top: 1rem;
  right: 1rem;
  font-size: 15px;
}

.no-border {
  border:0;
}

.exchange {
  color: #F0AD4E;
  font-size: 18px;
  cursor: pointer;
}

@media only screen and (max-width: 1440px) {
  .rate {
    font-size: 26px;
  }
}
```

- **widget.component.ts**:

```JavaScript
import { Component, OnInit, Input, Output, EventEmitter, OnDestroy } from '@angular/core';
import { Widget } from 'src/app/models/widget';
import { TradeService } from 'src/app/services/trade.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-widget',
  templateUrl: './widget.component.html',
  styleUrls: ['./widget.component.css']
})
export class WidgetComponent implements OnInit, OnDestroy {
  tenors = ['SP', '1M', '3M'];
  unsubscribe = new Subject();
  buyRateTrend: string;
  sellRateTrend: string;
  
  @Input() widget: Widget;
  @Input() index: number;
  @Input() currencies: string[];
  @Output() deleted = new EventEmitter<number>();

  constructor(
    private tradeService: TradeService
  ) { }

  ngOnInit() {
  }

  onDelete() {
    this.deleted.emit(this.index);
  }

  onSell() {
    const { notional, tenor } = this.widget;
    if (notional && tenor) {
      const username: string  = JSON.parse(localStorage.getItem('currentUser')).username;
      this.tradeService.saveTransaction({
      username: username,
      primaryCcy: this.widget.primaryCcy,
      secondaryCcy: this.widget.secondaryCcy,
      rate: this.widget.sellRate,
      action: 'sell',
      notional: this.widget.notional,
      tenor: this.widget.tenor,
      date: Math.round(new Date().getTime()/1000)
      }).subscribe(response => {
        console.log('Transaction saved', response)
      })
    }
  }
  
  onBuy() {
    const { notional, tenor } = this.widget;
    if (notional && tenor) {
      const username: string  = JSON.parse(localStorage.getItem('currentUser')).username;
      this.tradeService.saveTransaction({
      username: username,
      primaryCcy: this.widget.primaryCcy,
      secondaryCcy: this.widget.secondaryCcy,
      rate: this.widget.buyRate,
      action: 'buy',
      notional: this.widget.notional,
      tenor: this.widget.tenor,
      date: Math.round(new Date().getTime()/1000)
      }).subscribe(response => {
        console.log('Transaction saved', response)
      })
    }
  }

  onCCYChange() {
    this.switchCCY()
  }
  
  switchCCY() {
    const tempCCY = this.widget.primaryCcy;
    this.widget.primaryCcy = this.widget.secondaryCcy;
    this.widget.secondaryCcy= tempCCY;
  }

  startPooling() {
    const { primaryCcy, secondaryCcy } = this.widget;
    this.tradeService.getFxRatePolling(primaryCcy, secondaryCcy)
      .pipe(takeUntil(this.unsubscribe))
      .subscribe((response) => {

        this.buyRateTrend = this.widget.buyRate > response.buyRate ? 'down' : 'up'
        this.sellRateTrend = this.widget.sellRate > response.sellRate ? 'down' : 'up'

        this.widget.buyRate = response.buyRate;
        this.widget.sellRate = response.sellRate;
      });
  }

  onPickCurrency() {
    const { primaryCcy, secondaryCcy } = this.widget;
    if (primaryCcy && secondaryCcy && primaryCcy !== secondaryCcy) {
      this.widget.pickCCYState = false;
      this.startPooling();
    }
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
    }
}

```

In *Widget Component*, we can see many functionalities implemented:

- *onDelete*: when the user removes a widget
- *onSell*: save the transaction with *sell* action
- *onBuy*: save the transaction with *buy* action
- *onCCYChange*: switch the primary currency with the secondary one
- *startPooling*: get FX Rates through pooling to simulate real-time behavior
- *onPickCurrency*: when a new widget is added with primary and secondary currencies, start pooling FX Rates

### FX Rates View component

FX Rates View component is the right-side of the screen, containing all *Widget* Components.

- **fx-rates-view.component.html**:

```HTML
<h4 class="title">Fx Rates View</h4>
<div class="container-widget">
  <app-widget
    *ngFor="let widget of widgets; let i=index"
    class="widget"
    [widget]="widget"
    [index]="i"
    [currencies]="currencies"
    (deleted)="onDeleteWidget($event)"
  >
  </app-widget>
  <button class="button-widget" (click)="onAddWidget()">
    <span class="fa fa-plus button-plus"></span>
  </button>
</div>

```

So, as we can see:

- we use *Widget* Component (*app-widget*) for all widgets we have
- we have the possibility to add a new widget by clicking on a button

- **fx-rates-view.component.css**:

```CSS
.container-widget {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  padding-right: 2rem;
}

.button-widget {
  width: 47%;
  height: 250px;
  border: 1px solid grey;
  border-radius: 5px;
  padding: 10px;
}

.button-plus {
  font-size: 60px;
  color: grey;
}

.widget {
  display: flex;
  flex-basis: 47%;
  border: 1px solid grey;
  border-radius: 5px;
  margin-bottom: 2.5rem;
  height: 297px;
}

@media only screen and (max-width: 1440px) {
  .rate {
    font-size: 26px;
  }
}
```

- **fx-rates-view.component.ts**:

```JavaScript
import { Component, OnInit } from '@angular/core';
import { Widget } from 'src/app/models/widget';
import { TradeService } from 'src/app/services/trade.service';

@Component({
  selector: 'app-fx-rates-view',
  templateUrl: './fx-rates-view.component.html',
  styleUrls: ['./fx-rates-view.component.css']
})
export class FxRatesViewComponent implements OnInit {
  widgets: Widget[] = [];
  currencies: string[] = [];

  constructor(
    private tradeService: TradeService
  ) { }

  ngOnInit() {
    this.tradeService.getCurrencies().subscribe((response) => {
      this.currencies = response;
    })
  }

  onAddWidget() {
    this.widgets = [...this.widgets, new Widget('', '', 0, 0, null, '', true)]
  }
  
  onDeleteWidget(index: number) {
    this.widgets.splice(index, 1);
  }
}
```

In this class:

- we get all currencies from backend at initialization
- when a new widget is added, a new *Widget* component is created with default/empty values
- when a widget is removed, *onDeleteWidget* method is called, which uses the JavaScript *splice* method