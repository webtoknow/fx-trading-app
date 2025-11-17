# Frontend Development: Introduction

![You are here](../frontend.png)

## Table of contents

- [Components](#components)
- [Data Binding](#data-binding)
- [Pipes](#pipes)
- [Directives](#directives)
  - [Structural Directives (*ngIf, *ngFor)](#structural-directives)
  - [Attribute Directives](#attribute-directives)
  - [Control Flow (Angular 17+)](#control-flow-angular-17)
- [Routing](#routing)
- [Services](#services)
  - [Http Service](#http-service)
- [Dependency Injection](#dependency-injection)
- [inject() Function (Angular 14+)](#inject-function-angular-14)
- [Route Guards](#route-guards)
- [HTTP Interceptors](#http-interceptors)

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

#### Structural Directives

**\*ngIf** - Conditionally display content:

```HTML
<div *ngIf="isLoggedIn">
  <p>Welcome back!</p>
</div>

<div *ngIf="isLoggedIn; else loginPrompt">
  <p>Welcome back!</p>
</div>
<ng-template #loginPrompt>
  <p>Please log in.</p>
</ng-template>
```

**\*ngFor** - Iterate over collections:

```HTML
<tr *ngFor="let transaction of transactions">
  <td>{{ transaction.id }}</td>
  <td>{{ transaction.username }}</td>
  <td>{{ transaction.CcyPair }}</td>
  <td>{{ transaction.rate | number }}</td>
  <td>{{ transaction.action }}</td>
  <td>{{ transaction.notional }}</td>
  <td>{{ transaction.tenor }}</td>
  <td>{{ transaction.date | date:'dd/MM/yyyy HH:mm' }}</td>
</tr>
```

- You can access the index and other loop variables:

```HTML
<div *ngFor="let item of items; let i = index">
  {{ i + 1 }}. {{ item.name }}
</div>
```

#### Attribute Directives

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

### Control Flow (Angular 17+)

**Note**: This section covers new Angular 17+ syntax. The exercises in this laboratory use the traditional structural directives shown above (*ngIf, *ngFor).

- **Angular 17** introduced a new built-in control flow syntax as an alternative to structural directives.
- The new syntax uses `@if`, `@else`, `@for`, and `@switch` blocks for better performance and readability.

#### @if and @else

```HTML
@if (isLoggedIn) {
  <p>Welcome back!</p>
} @else {
  <p>Please log in.</p>
}
```

#### @for

```HTML
@for (transaction of transactions; track transaction.id) {
  <tr>
    <td>{{ transaction.id }}</td>
    <td>{{ transaction.username }}</td>
    <td>{{ transaction.amount }}</td>
  </tr>
} @empty {
  <p>No transactions found.</p>
}
```

- The `track` expression is required and helps Angular identify which items changed for efficient rendering.

**Important**: Both approaches work in Angular 17+. This laboratory uses the traditional structural directives (*ngIf, *ngFor) which are still fully supported and widely used.

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

- The HttpClient is used to perform HTTP operations.
- For using Http Service, we need to provide it in *app.module.ts*

```JavaScript
import { provideHttpClient } from '@angular/common/http';

@NgModule({
     imports: [
        ...
     ],
     providers: [
        provideHttpClient(),
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
- Dependencies can be services or objects required by a class to perform its function.

**Constructor-based Injection (Traditional approach)**

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

### inject() Function (Angular 14+)

- **Angular 14** introduced the `inject()` function, which allows dependency injection outside of constructors.
- This enables a more flexible and functional approach to dependency injection.

**Using inject() in Components**

```JavaScript
import { Component, inject } from '@angular/core';
import { TradeService } from '../../../services/trade.service';

@Component({
  selector: 'app-blotter-view',
  templateUrl: './blotter-view.component.html',
  styleUrls: ['./blotter-view.component.css']
})
export class BlotterViewComponent {
  private tradeService = inject(TradeService);

  ngOnInit() {
    this.tradeService.getTransactions();
  }
}
```

**Using inject() in Functions**

```JavaScript
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const myUtilityFunction = () => {
  const router = inject(Router);

  return router.createUrlTree(['/dashboard']);
};
```

**Benefits of inject()**
- Cleaner code with less boilerplate
- Easier to use in functional contexts (guards, interceptors, factory functions)
- Better tree-shaking for smaller bundle sizes
- More flexible - can be used in class fields, functions, and factories

**Note**: Both constructor injection and `inject()` function work in Angular 14+. The `inject()` function is particularly useful in functional guards, interceptors, and when creating reusable utility functions. Constructor injection is still the standard for components and services in most cases.

### Route Guards

- Route guards are used to control access to routes based on certain conditions (e.g., authentication, authorization).
- Guards implement specific interfaces like `CanActivate`, `CanDeactivate`, `CanActivateChild`, etc.

#### Class-based Guard (Traditional approach)

```JavaScript
import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (localStorage.getItem('currentUser')) {
      // User is logged in, allow access
      return true;
    }

    // User is not logged in, redirect to login page
    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
}
```

#### Functional Guard (Angular 15+)

- **Angular 15** introduced functional guards, which offer a simpler, more flexible approach.
- Functional guards use the `inject()` function (introduced in Angular 14) to access dependencies:

```JavaScript
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const authGuard = () => {
  const router = inject(Router);

  if (localStorage.getItem('currentUser')) {
    return true;
  }

  return router.createUrlTree(['/login']);
};
```

#### Using Guards in Routes

```JavaScript
const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  // Or with functional guard:
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] }
];
```

**Note**: Functional guards are recommended for Angular 15+ as they are more concise and easier to test. Class-based guards still work but are considered the legacy approach.

### HTTP Interceptors

- HTTP Interceptors allow you to intercept and modify HTTP requests and responses globally.
- Common use cases include adding authentication tokens, logging, error handling, and caching.

#### Class-based Interceptor (Traditional approach)

```JavaScript
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Add authorization header with jwt token if available
    let currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (currentUser && currentUser.token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${currentUser.token}`
        }
      });
    }

    return next.handle(request);
  }
}
```

- Register the interceptor in `app.module.ts`:

```JavaScript
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './helpers/jwt.interceptor';

@NgModule({
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ]
})
```

#### Functional Interceptor (Angular 15+)

- **Angular 15** introduced functional interceptors for a more streamlined approach.
- Functional interceptors can also use the `inject()` function to access services if needed:

```JavaScript
import { HttpInterceptorFn } from '@angular/common/http';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');

  if (currentUser && currentUser.token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${currentUser.token}`
      }
    });
  }

  return next(req);
};
```

- Register functional interceptor using `provideHttpClient`:

```JavaScript
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { jwtInterceptor } from './helpers/jwt.interceptor';

@NgModule({
  providers: [
    provideHttpClient(
      withInterceptors([jwtInterceptor])
    )
  ]
})
```

**Note**: Functional interceptors are recommended for Angular 15+ as they are simpler and more composable. Class-based interceptors still work but are considered the legacy approach.
