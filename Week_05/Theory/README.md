# Week 5 - Development

## Table of contents

- [Components](#components)
- [Data Binding](#data-binding)
- [Pipes](#pipes)
- [Directives](#directives)
  - [Structural Directives](#structural-directives)
  - [Attribute Directives](#attribute-directives)
- [Routing](#routing)
- [Services](#services)
- [Dependency Injection](#dependency-injection)

## About Angular - part I

- is a JavaScript framework that makes web applications to be easy to build
- comes with features like data-binding, change-detection, forms, router&navigation and http implementation

### Components

- is the most basic building block in Angular world
- basically, components are classes that interact with the *.html* file of the component - which is displayed in the browser
- a component's application logic is defined inside a class
- the class interacts with the view through some properties and methods
- we use **@Component** decorator to identify a class as a component class
- we need to specify the component's metadata. For example:

```JavaScript
@Component({
  selector: 'app-blotter-view',
  templateUrl: './blotter-view.component.html',
  styleUrls: ['./blotter-view.component.css']
})
```

- after declaring the component, it will be specified into the HTML file of its parent in order to be loaded:

```Html
<app-blotter-view></app-blotter-view>
```

### Data Binding

- data binding is available from AngularJS
- we use curly braces for data binding - **{{ }}** :

```Html
 <tr *ngFor="let transaction of transactions">
    <td>{{ transaction.id }}</td>
    <td>{{ transaction.username }}</td>
    <td>{{ transaction.CcyPair }}</td>
    <td>{{ transaction.rate | number }}</td>
    <td>{{ transaction.action  }}</td>
    <td>{{ transaction.notional }}</td>
    <td>{{ transaction.tenor }}</td>
    <td>{{ transaction.date | date:'dd/MM/yyyy HH:mm' }}</td>
 </tr>
```

- this process is called **interpolation**
- Angular supports **two-way data binding** - a mechanism for coordinating parts of a template with parts of a component
- plays an important role in communication between:
  - a template and its component
  - a parent and its child components

### Pipes

- let us declare display-value transformations in HTML template, by using the pipe operator ( **|** ):

```Html
 <td>{{ transaction.date | date:'dd/MM/yyyy HH:mm' }}</td>
```

- we can declare our own pipes: a class with **@Pipe** decorator defines a function that transforms input values to output values for display in a view :

```JavaScript
import { Pipe, PipeTransform } from '@angular/core';
/*
 * Raise the value exponentially
 * Takes an exponent argument that defaults to 1.
 * Usage:
 *   value | exponentialStrength:exponent
 * Example:
 *   {{ 2 | exponentialStrength:10 }}
 *   formats to: 1024
*/
@Pipe({name: 'exponentialStrength'})
export class ExponentialStrengthPipe implements PipeTransform {
  transform(value: number, exponent: string): number {
    let exp = parseFloat(exponent);
    return Math.pow(value, isNaN(exp) ? 1 : exp);
  }
}
```

### Directives

- templates are dynamic - when Angular renders them, it transforms the DOM according to the instructions given by directives
- a directive is a class with a **@Directive** deorator
- each component is a directive - but components are distinctive
- there are two kinds of directives besides components: **structural** and **attribute** directives

#### Structural Directives

- manipulate DOM elements
- have a * sign before the directive
- *e.g.* : *ngIf, *ngFor

#### Attribute Directives

- change the look and the behavior of the DOM element
- you can create your own directives:

```JavaScript
import { Directive, ElementRef } from '@angular/core';

@Directive({
  selector: '[appHighlight]'
})
export class HighlightDirective {
    constructor(el: ElementRef) {
       el.nativeElement.style.backgroundColor = 'yellow';
    }
}
```

### Routing

- means navigating between pages
- through routing, we can have links that direct us to a new page
- we are reffering to these pages as components:

```JavaScript
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RegisterPageComponent } from './pages/register-page/register-page.component';
import { DashboardPageComponent } from './pages/dashboard-page/dashboard-page.component';
import { NotFoundPageComponent } from './pages/not-found-page/not-found-page.component';

...

const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full', canActivate: [AuthGuard] },
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'dashboard', component: DashboardPageComponent, canActivate: [AuthGuard] },
  { path: '**', component: NotFoundPageComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    RegisterPageComponent,
    DashboardPageComponent,
    NotFoundPageComponent,
    ...
  ],
  imports: [
      RouterModule.forRoot(appRoutes),
      ...
  ],
  ...
})

export class AppModule { }
```

```Html
<router-outlet></router-outlet>
```

### Services

- if we are in a situation where we need some code to be used everywhere on the page, we should use services *e.g. : Data connections*)
- through services, methods and components can be accessed across other components in the whole project
- a service should look like this: 

```JavaScript
@Injectable()
export class UserService {
    constructor() { }

    // some methods and properties
}
```

#### Http Service

- used for fetching external data, post data etc.
- for using Http Service, we need to import this module in *app.module.ts*

```JavaScript
import { HttpClientModule } from '@angular/common/http';

@NgModule({
     imports: [
        HttpClientModule,
        ...
     ],
     ...
})
```

- this is the way we can make a service which use **HttpClient**:

```JavaScript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';

import { Transaction } from 'src/app/models/transaction';

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

### Dependency Injection

- is the way to create objects that depend upon other objects
- a Depencency Injection system supplies the dependent objects (called also **dependencies**) when it creates an instance of an object
- depencencies are services or objects that a class needs to perform its function
- in Angular, Dependency Injection provides declared dependencies to a class when that class is instantiated:

```JavaScript
import { TradeService } from '../../../services/trade.service';

@Component({
  selector: 'app-blotter-view',
  templateUrl: './blotter-view.component.html',
  styleUrls: ['./blotter-view.component.css']
})
export class BlotterViewComponent {
  constructor(
    private tradeService: TradeService
  ) { }
  ...
}
```
