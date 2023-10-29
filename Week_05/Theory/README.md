# Week 5 - Create Login and Register pages with Angular

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

- Angular is a powerful JavaScript framework that simplifies web application development.
- Key features of Angular include data-binding, change-detection, forms, routing and navigation, and HTTP implementation.

### Components

- Component is the fundamental building block in Angular.
- Components are classes that interact with an associated *HTML* file, which is rendered in the browser.
- The application logic of a component is defined within its class.
- Interaction with the view is accomplished through properties and methods.
- Components are identified using the **@Component** decorator.
- Component metadata, such as the selector, template URL, and style URLs, must be specified. Example:

```JavaScript
@Component({
  selector: 'app-blotter-view',
  templateUrl: './blotter-view.component.html',
  styleUrls: ['./blotter-view.component.css']
})
```

- After declaring a component, it can be used in the HTML of its parent component using its selector:

```Html
<app-blotter-view></app-blotter-view>
```

### Data Binding

- Data binding is a powerful feature inherited from AngularJS.
- Angular supports **interpolation** for data binding using curly braces - **{{ }}** :

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

- Angular also supports **two-way data binding** for interactive UI elements:

```HTML
<input [(ngModel)]="username">

<p>Hello {{username}}!</p>
````

### Pipes

- Pipes allow you to transform and format data for display in the HTML template.
- Angular provides several built-in pipes like **date**, **currency**, and **uppercase**:

```Html
 <td>{{ transaction.date | date:'dd/MM/yyyy HH:mm' }}</td>
```

- You can create custom pipes by defining a class with the **@Pipe** decorator:

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

- Templates in Angular are dynamic, and directives instruct Angular on how to transform the DOM.
- To create a custom attribute directive, use the **@Directive** decorator.
- Directives can be either structural (e.g., *ngIf, *ngFor) or attribute (e.g., custom directives).

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

### Data Sharing

- Data sharing between parent and child components is a common pattern in Angular.
- **@Input()** is used to decorate properties in a child component, and it receives data from the parent component.

```JavaScript
  // widget.component.ts 
  @Input() index: number = 0;
  @Output() deleted = new EventEmitter<number>();
```

- The value of *index* property for each Widget instance comes from the parent component template.
- The *index* property is binded to the child (*WidgetComponent*) to the *i* property of the parent (*FxRatesViewComponent*).

```Html
<!-- fx-rates-view.component.html -->
  <app-widget
    *ngFor="let i=index"
    [index]="i"
  >
  </app-widget>
```

- **@Output()** marks a property in a child component as an event emitter, allowing data to flow from the child to the parent:

```JavaScript
  // widget.component.ts 
  @Output() deleted = new EventEmitter<number>();
  ...
  this.deleted.emit(this.index);
```

- The *onDeleteWidget* property from the parent, is binded to the child event (*deleted*).

```Html
<!-- fx-rates-view.component.html -->
  <app-widget
    *ngFor="let i=index"
    [index]="i"
    (deleted)="onDeleteWidget($event)"
  >
  </app-widget>
```

### Routing

- Angular provides powerful routing capabilities for navigating between different pages (components).
- Define routes and components in the routing configuration:

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
- Use the **<router-outlet></router-outlet>** element to display routed components.
- 
### Services

- Services are used to encapsulate functionality that needs to be shared across components.
- A service typically contains methods and properties that can be accessed by multiple components.
- Define services using the **@Injectable()** decorator.

```JavaScript
@Injectable()
export class UserService {
    constructor() { }

    // some methods and properties
}
```

#### Http Service

- The HttpClientModule is used to perform HTTP operations.
- For using Http Service, we need to import this module in *app.module.ts*

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

- Define an HTTP service that makes requests and handles responses from external sources:

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

- Angular uses **dependency injection** to provide dependencies to a class when an instance of that class is created.
- Dependencies can be services or objects required by a class to perform its function.:

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
