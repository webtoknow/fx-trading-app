# Week 5 - Create Login and Register pages with Angular

## Table of contents
- [Exercise 0 - Configure and start Mock Server](#exercise-0---configure-and-start-mock-server)
- [Exercise 1 - Pages, Routing and Navigation](#exercise-1---pages-routing-and-navigation)
  - [Create pages](#create-pages)
  - [Add routes](#add-routes)
  - [Add Toastr](#add-toastr)
  - [Fill in global style file](#fill-in-global-style-file)
  - [Add Bootstrap, Datepicker and Fontawesome](#add-bootstrap-datepicker-and-fontawesome)
- [Exercise 2 - Register page](#exercise-2---register-page)
  - [User model](#user-model)
  - [Constants file](#constants-file)
  - [User service](#user-service)
  - [Update Application Module](#update-application-module)
  - [Register component](#register-component)
- [Exercise 3 - Login page](#exercise-3---login-page)
  - [Authentication service](#authentication-service)
  - [Authentication guard](#authentication-guard)
  - [JWT Interceptor](#jwt-interceptor)
  - [Error Interceptor](#error-interceptor)
  - [Update Application Module with the new added classes](#update-application-module-with-the-new-added-classes)
  - [Login component](#login-component)
- [Exercise 4 - Not found page](#exercise-4---not-found-page)

## Exercise 0 - Configure and start Mock Server

Mock server is used to create a fake API to mock the backend data using [JSON Server](https://github.com/typicode/json-server).

Let's install its packages:

```bash
cd fx-trading-app\Week_05\Exercise\Code\mock-server
npm install
```

Start all microservices in a single terminal:

```bash
npm start
```

Now we can access these APIs:

- `http://localhost:8200/user/authenticate` - sign-in
- `http://localhost:8200/user/register` - register
- `http://localhost:8210/transactions` - get all transactions
- `http://localhost:8220/currencies` - get all currencies
- `http://localhost:8220/fx-rate` - get fx rates for specific currencies



## Exercise 1 - Pages, Routing and Navigation

### Create pages

Go to *Week_05/Exercise/Code/ui*:

```bash
cd fx-trading-app\Week_05\Exercise\Code\ui
```

Run *npm install* to download all dependencies:

```bash
npm install
```

Create a folder for pages in *ui/src/app*:

```bash
cd src\app
mkdir pages
cd pages
```

Generate page components using CLI:

```bash
ng generate component dashboard-page
ng generate component login-page
ng generate component not-found-page
ng generate component register-page
```

### Add routes

In *app-routing.module* import all the components you have to make them available for routing and then update the routes array by linking all our components:

```JS
...
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RegisterPageComponent } from './pages/register-page/register-page.component';
import { DashboardPageComponent } from './pages/dashboard-page/dashboard-page.component';
import { NotFoundPageComponent } from './pages/not-found-page/not-found-page.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'dashboard', component: DashboardPageComponent },
  { path: '**', component: NotFoundPageComponent }
];
...
```
Now we need to clean up de project by:
- Removing the startup project markup from *app.component.html* and only keeping `<router-outlet></router-outlet>`.
- Removing `title = 'ui'` from AppComponent in *app.component.ts*.


### Add Toastr

We are using [ngx-toastr](https://github.com/scttcper/ngx-toastr) for notifications. 

Install *ngx-toastr*:

```bash
npm install ngx-toastr@15.2.1
npm install @angular/animations
```

In order to load *toast* style, we should update *angular.json*:

```JSON
"styles": [
 "src/styles.css" // already here
  "node_modules/ngx-toastr/toastr.css" // add this
]
```

We should also update *app.module.ts* add ToastrModule, make sure you have BrowserAnimationsModule as well:

```JavaScript
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
...
imports: [
    ...,
    BrowserAnimationsModule,
    ToastrModule.forRoot()
  ],
...
```

More info how can we use ngx-toastr we can find on [github](https://github.com/scttcper/ngx-toastr#use).

### Fill in global style file

Open *styles.css* global style file and fill in with these classes, used in the whole application, not only in one specific component:

```CSS
/* You can add global styles to this file, and also import other style files */

html, body{
    height: 100%;
    color: #373A3C;
    font-family: 'Open Sans', sans-serif;
}

h1,h2,h3,h4,h5,h6 {
    color: #7C7C7C;
}

.btn-primary {
    background-color: #3496F0;
}

.btn-link {
    color: #3496F0;
}
.table-striped tbody tr:nth-of-type(odd) {
    background-color: #F2F2F2;
}

.flex {
    display: flex;
}

.flex-vertical-centered {
    display: flex;
    align-items: center;
}

.title {
    margin-bottom: 30px;
    padding-bottom: 20px;
    font-weight: bold;
}

.title-border {
    border-bottom: 1px solid #DDDDDD;
}

.screen-full-height {
    height: 100vh;
    display: flex;
}

.is-invalid {
  border-left: 5px solid #D9534F;
}
```

### Add Bootstrap, Datepicker and Fontawesome

In order to load Open Sans font, bootstrap and Fontawesome style, we should update *index.html* with CSS links:

```HTML
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;700&display=swap" rel="stylesheet">

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We">
<link rel="stylesheet" href="https://unpkg.com/ngx-bootstrap/datepicker/bs-datepicker.css">

<script defer src="https://use.fontawesome.com/releases/v5.15.4/js/all.js"
    integrity="sha384-rOA1PnstxnOBLzCLMcre8ybwbTmemjzdNlILg8O7z1lUkLXozs4DHonlDtnE7fpc"
    crossorigin="anonymous"></script>
```

and also install ngx-bootstrap:

```bash
npm install ngx-bootstrap@9.0.0
```

Link ngx-bootstrap by updating *app.module.ts*:

```JavaScript
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';

imports: [
    ...
    BsDatepickerModule.forRoot()
]
```

## Exercise 2 - Register page

### User model

First of all, let's create a new folder into *Week_05/Exercise/Code/ui/src/app* named *models* containing all entities we will need.

By taking a look at the register page's design, we can identify the required fields for user entity:

- id
- username
- email
- password

So, create a new file *user.ts* containing the fields above:

```JavaScript
export class User {
    id: number | null = null;
    username: string = '';
    email: string = '';
    password: string = '';
}
```

### Constants file

We will need to communicate with backend through some API's. Let's estabilish how they will look like.

On server side, we will have 3 micro-services:

- *authService*, running on port 8200
- *fxTradeService*, running on port 8210
- *quoteService*, running on port 8220

Into *Week_05/Exercise/Code/ui/src/app*, we will create a new file, *constants.ts*, containing all API's needed:

```JavaScript
export const authApi = 'http://localhost:8200'
export const tradeApi = 'http://localhost:8210'
export const quoteApi = 'http://localhost:8220'

export const backendUrl = {
  authService: {
    authenticate: `${authApi}/user/authenticate`,
    register: `${authApi}/user/register`,
  },
  fxTradeService: {
    getTransactions: `${tradeApi}/transactions`,
    saveTransaction: `${tradeApi}/transactions`,
  },
  quoteService: {
    getCurrencies: `${quoteApi}/currencies`,
    getFxRate: `${quoteApi}/fx-rate`
  }
}
```

### User service

Let's focus now on the service. We want to send the user details through HTTP request to the server.

First, let's make a new folder into *Week_05/Exercise/Code/ui/src/app* named *services*. It will contain all required services for our functionalities.

Our first service will be *user.service.ts*:

```JavaScript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { User } from '../models/user';
import { backendUrl } from '../constants';

@Injectable()
export class UserService {
  constructor(private http: HttpClient) { }

  register(user: User) {
    return this.http.post(backendUrl.authService.register, user) as any;
  }
  
}
```

So, the service:

- will be marked as injectable through *@Injectable()* annotation
- will contain a constructor, in which we will inject *HTTPClient* in order to be able to make HTTP requests
- will contain also the *register* method, receiving an *User* object - the one we want to save in order to create a new account - which will actually do the HTTP request: a **POST** on the URL established in *constants.ts* (*backendUrl.authService.register*) with *user* as *Request Body*

### Update Application Module

In *app.module.ts*:

- add *Forms* module in order to use them:

```JavaScript
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

 imports: [
    ...,
    FormsModule,
    ReactiveFormsModule,
    ...
 ]
```

- include *Http Client*:

```JavaScript
import { HttpClientModule } from '@angular/common/http';

imports: [
    ...,
    HttpClientModule,
    ...
  ]
```

- include *User Service*:

```JavaScript
import { UserService } from 'src/app/services/user.service';

 providers: [
    ...
    UserService,
    ...
  ]
```

### Register component

We already created the necessary files for register functionality. Now we have to fill in with the right code in order to do what we want to do.

- **register-page.component.html**:

```HTML
<div class="screen-full-height">
  <div class="col-md-6 login-logo-container container-center">
    <img class="fx-grayscale-logo" alt="fx-logo" src="./assets/img/logo-grayscale.svg">
  </div>
  <div class="col-md-6">
    <div class="container-center screen-full-height">
      <div class="content">
        <h4 class="title title-border">
          Register a new account
        </h4>
        <form id="register" [formGroup]="registerForm" (ngSubmit)="onSubmit()">
          <div class="mb-3">
            <label for="username">Username</label>
            <input type="text" class="form-control form-control-sm" id="inputUsername" formControlName="username"
              placeholder="username" autocomplete="username"
              [ngClass]="{ 'is-invalid': submitted && f['username'].errors }" />
            <span *ngIf="submitted && f['username'].errors" class="invalid-feedback">
              <span *ngIf="f['username'].errors['required']">Username is required!</span>
            </span>
          </div>
          <div class="mb-3">
            <label for="email">Email</label>
            <input type="text" class="form-control form-control-sm" id="inputEmail" formControlName="email"
              placeholder="email address" [ngClass]="{ 'is-invalid': submitted && f['email'].errors }" />
            <div *ngIf="submitted && f['email'].errors" class="invalid-feedback">
              <div *ngIf="f['email'].errors['required']">Email is required!</div>
            </div>
          </div>
          <div class="mb-3">
            <label for="password">Password</label>
            <input type="password" class="form-control form-control-sm" id="inputPassword" formControlName="password"
              placeholder="password" autocomplete="new-password"
              [ngClass]="{ 'is-invalid': submitted && f['password'].errors }" />
            <div *ngIf="submitted && f['password'].errors" class="invalid-feedback">
              <div *ngIf="f['password'].errors['required']">Password is required!</div>
              <div *ngIf="f['password'].errors['minlength']">Password must be at least 6 characters!
              </div>
            </div>
          </div>
          <div class="mb-3">
            <label for="password">Confirm password</label>
            <input type="password" class="form-control form-control-sm" id="inputConfirmPassword"
              formControlName="confirmPassword" placeholder="confirm password" autocomplete="new-password"
              [ngClass]="{ 'is-invalid': submitted && f['confirmPassword'].errors }" />
            <div *ngIf="submitted && f['confirmPassword'].errors" class="invalid-feedback">
              <div *ngIf="f['confirmPassword'].errors['required']">Password confirmation is required!
              </div>
            </div>
          </div>
          <button [disabled]="loading" type="submit" class="btn btn-primary btn-block">Register</button>
          <div class="text-container">
            <span>Already have an account?&nbsp;</span>
            <a [routerLink]="'/login'" class="btn btn-link">Login</a>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
```

We can notice here:

- fields are grouped in a form: *[formGroup]="registerForm"*
- when we press on *Register*, the *onSubmit* function is triggered: *(ngSubmit)="onSubmit()"*
- we have some validations here:
- required validations:
- *e.g.*:

```HTML
<span *ngIf="submitted && f['username'].errors" class="invalid-feedback">
  <span *ngIf="f['username'].errors['required']">Username is required!</span>
</span>
```

- minimum length validations:
- *e.g.*:

```HTML
<div *ngIf="submitted && f['password'].errors" class="invalid-feedback">
  <div *ngIf="f['password'].errors['required']">Password is required!</div>
  <div *ngIf="f['password'].errors['minlength']">Password must be at least 6 characters!</div>
</div>
```

- we have a link to *Login Page*:

```HTML
<div class="text-container">
  <span>Already have an account?&nbsp;</span>
  <a [routerLink]="'/login'" class="btn btn-link">Login</a>
</div>
```

- **register-page.component.css**:

```CSS
.container-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-logo-container {
  background-color: rgb(141, 213, 170);
}

.fx-grayscale-logo {
  width: 350px;
  height: 350px;
}

.content {
  width: 350px;
}

.col {
  padding: 0;
}

.invalid-feedback {
  font-weight: bold;
}

.btn-link {
  padding: 0;
}

.text-container {
  margin-top: 5px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
```

- **register-page.component.ts**:

```JavaScript
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';

import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.css']
})
export class RegisterPageComponent implements OnInit {

  registerForm: FormGroup = this.formBuilder.group({
    username: ['', Validators.required],
    email: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', Validators.required]
  }, {
    validator: this.mustMatch('password', 'confirmPassword')
  });

  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
  }

  // Convenience getter for easy access to form fields
  get f() { return this.registerForm.controls; }

  // custom validator to check that two fields match
  mustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors['mustMatch']) {
        // return if another validator has already found an error on the matchingControl
        return;
      }

      // set error on matchingControl if validation fails
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ mustMatch: true });
      } else {
        matchingControl.setErrors(null);
      }
    }
  }
  onSubmit() {
    this.submitted = true;

    // Exit function if form is invalid
    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;
    this.userService.register(this.registerForm.value)
      .pipe(first())
      .subscribe(
        (data: any) => {
          this.toastr.success('Registration successful!');
          this.loading = false;
          this.router.navigate(['/login']);
        },
        (error: string) => {
          this.toastr.error(error);
          this.loading = false;
        }
      )
  }

}
```

We can notice here:

- the component is declared through *@Component* annotation, by specifying the selector, template and style files
- the form, its fields and validations are specified in the class
- in *onSubmit* function, if the form is valid, we use *userService.register* to send the entity to be saved. If the request is successful, we display a message and redirect the user to *login* page, but if it is not, we just display an appropriate message.

## Exercise 3 - Login page

### Authentication service

This service will be responsible for logging the user in and out, by putting on *localStorage* the *currentUser* or by removing it.

So, in *Week_05/Exercise/Code/ui/src/app/services*, we will create a new file, *authentication.service.ts*:

```JavaScript
// used for login and logout of the application
// login -> posts the users credentials to api and checks the response for a JWT token
// logged in user details are stored in local storage

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { backendUrl } from '../constants';

@Injectable()
export class AuthenticationService {

  constructor(
    private http: HttpClient
  ) { }

  login(username: string, password: string) {
    return this.http.post<any>(backendUrl.authService.authenticate, { username: username, password: password })
      .pipe(map(user => {
        // login successful if there's a jwt token in the response
        if (user && user.token) {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify(user));
        }
        return user;
      }))
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('currentUser');
  }

}
```

This service:

- will be marked as injectable as the previous one
- in the constructor, we will inject *HTTPClient* in order to have the possibility to make HTTP requests
- will contain 2 methods: *login* and *logout*
- *login* method will receive as parameters 2 strings, username and password. It will make a *POST* HTTP request to the API responsible for this (*backendUrl.authService.authenticate*, as declared in *constants*) and will send a *Request Body* as on object containing these 2 properties. If they are valid, the backend will put a token on the response and we will set the *currentUser* on *localStorage* to log in.
- *logout* method will remove the *currentUser* property from localStorage

### Authentication guard

We need a method to allow the user to view some pages only if he is logged in.

For this purpose, we will create a new folder into *Week_05/Exercise/Code/ui/src/app* named *guards* and, inside it, a new file, *auth.guard.ts*. This class will be responsible to check if the user has access to view the  pages linked with some routes or not. It will be possible by verifying if the *currentUser* property has been set on *localStorage*. If yes, the access is permitted, else, the user will be redirected to */login* page:

```JavaScript
import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (localStorage.getItem('currentUser')) {
      // we are logged in => return true
      return true;
    }

    // we are not logged in => redirect to login page with the return url
    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }

}
```

After creating the class, we need to put it on the private routes. So, in *app-routing.module.ts*, we will update the routes declaration to be:

```JavaScript
import { AuthGuard } from 'src/app/guards/auth.guard';

const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'dashboard', component: DashboardPageComponent, canActivate: [AuthGuard] },
  { path: '**', component: NotFoundPageComponent }
];
```

This means that *dashboard* will be private and if the user is not logged in, he will be redirected to */login*

### JWT Interceptor

The next class will be responsible for intercepting HTTP requests from the application to add a JWT auth token to the Authentication header if the user is logged in.

For this, we will create a new folder into *Week_05/Exercise/Code/ui/src/app* named *helpers*. Inside it, we will create a new file, *jwt.interceptor.ts*:  

```JavaScript
// intercepts http requests from the application to add a JWT auth token to the Authentication header
// if the user is logged in

import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with jwt token if available
    let currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (currentUser && currentUser.token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${currentUser.token}`
        }
      })
    }

    return next.handle(request);
  }
}
```

### Error Interceptor

We will also need a class which intercepts the errors and handles them.

Let's create a new file *error.interceptor.ts* into *Week_05/Exercise/Code/ui/src/app/helpers*, *error.interceptor.ts*:

```JavaScript
// intercepts http responses from the api to check if there were any errors.
// If there is a 401 Unauthorized response the user is automatically logged out of the application.
// All other errors are re-thrown to be caught by the calling service so an alert will be displayed to the user

import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthenticationService } from '../services/authentication.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => {
      if (err.status === 401) {
        //logout if 401 response is returned from api
        this.authenticationService.logout();
      }

      const error = err.error.message || err.statusText;
      return throwError(error);
    }))
  }

}
```

So, the way we handle the errors into this class is:

- if the error has 401 status, means that the user is not authorized to view the page, so he will be logged out
- the error thrown will contain the message or the status

### Update Application Module with the new added classes

In *app.module.ts*:

- include *Authentication Service*:

```JavaScript
import { AuthenticationService } from 'src/app/services/authentication.service';

providers: [
    ...
    AuthenticationService,
    ...
```

- include *Authorization Guard*:

```JavaScript
import { AuthGuard } from 'src/app/guards/auth.guard';

providers: [
    AuthGuard,
    ...
```

- add *JWT* and *Error Interceptors*:

```JavaScript
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from 'src/app/helpers/jwt.interceptor';
import { ErrorInterceptor } from 'src/app/helpers/error.interceptor';

 providers: [
    ...
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],

```

### Login component

Let's fill in the necessary files for login functionality with some code in order to make these work:

- **login-page.component.html**:

```HTML
<div class="screen-full-height">
  <div class="col-md-6 login-logo-container container-center">
    <img class="fx-grayscale-logo" alt="fx-grayscale-logo" src="./assets/img/logo-grayscale.svg">
  </div>
  <div class="col-md-6">
    <div class="container-center screen-full-height">
      <div class="content">
        <h4 class="title title-border">
          Login to your account
        </h4>
        <form id="login" [formGroup]="loginForm" (ngSubmit)="onSubmit()">
          <!-- Username -->
          <div class="form-group flex mb-3">
            <i class="fa fa-user icon" aria-hidden="true"></i>
            <div class="col">
              <input type="text" class="form-control form-control-sm" formControlName="username"
                placeholder="username" autocomplete="username" id="username"
                [ngClass]="{ 'is-invalid': submitted && f['username'].errors }" />
              <span *ngIf="submitted && f['username'].errors" class="invalid-feedback">
                <span *ngIf="f['username'].errors['required']">Username is required!</span>
              </span>
            </div>
          </div>
          <!-- Password -->
          <div class="form-group flex mb-3">
            <i class="fa fa-lock icon" aria-hidden="true"></i>
            <div class="col">
              <input type="password" class="form-control form-control-sm" formControlName="password"
                placeholder="password" autocomplete="current-password" id="password"
                [ngClass]="{ 'is-invalid': submitted && f['password'].errors }" />
              <div *ngIf="submitted && f['password'].errors" class="invalid-feedback">
                <div *ngIf="f['password'].errors['required']">Password is required!</div>
              </div>
            </div>
          </div>
          <button [disabled]="loading" type="submit" class="btn btn-primary btn-block">Login</button>
          <div class="text-container">
            <span>You don't have an account?&nbsp;</span>
            <a [routerLink]="['/register']" class="btn btn-link">Register</a>
          </div>
        </form>
      </div>
    </div>
  </div>
```

Here we have:

- a form containing *username* and *password* fields, grouped in *[formGroup]="loginForm"*
- when we press on *Login*, the *onSubmit* function is triggered: *(ngSubmit)="onSubmit()"*
- we have here required validations:
  - *e.g.*:

  ```HTML
  <span *ngIf="submitted && f['username'].errors" class="invalid-feedback">
    <span *ngIf="f['username'].errors['required']">Username is required!</span>
  </span>
  ```

- we have a link to *Register Page*:

```HTML
<div class="text-container">
  <span>You don't have an account?&nbsp;</span>
  <a [routerLink]="['/register']" class="btn btn-link">Register</a>
</div>
```

- **login-page.component.css**:

```CSS
.container-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-logo-container {
  background-color: rgb(141,213,170);
}

.fx-grayscale-logo {
  width: 350px;
  height: 350px;
}

.content {
  width: 350px;
}

.icon {
  font-size: 24px;
  color: #dddddd;
  margin-right: 15px;
  line-height: 31px;
}

.col {
  padding: 0;
}

.invalid-feedback {
  font-weight: bold;
}

.btn-link {
  padding: 0;
}

.text-container {
  margin-top: 5px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
```

- **login-page.component.ts**:

```JavaScript
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  loginForm: FormGroup = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });
  loading = false;
  submitted = false;
  returnUrl: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {

    this.loginForm 

    this.authenticationService.logout();

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';

  }

  // Convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // Exit function if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authenticationService.login(this.f['username'].value, this.f['password'].value)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.toastr.error(error);
          this.loading = false;
        }
      )
  }

}
```

We can observe:

- the component is declared through *@Component* annotation
- the form, its fields and validations are specified in the class
- in *onSubmit* function, if the form is valid, we use *authenticationService.login* to send the username and password to the server. If the request is successful, we will be redirected to *Dashboard* page or the previous accessed page where we did not have access initially (*returnUrl*). Else, we will display an error message.


## Exercise 4 - Not found page

If the user access a route that does not exist, we should display a page containing the *Page not found* message.

- **not-found-page.component.html**:

```HTML
<div class="screen-full-height fx-not-found-container">
  <img class="fx-not-found-logo" alt="fx-not-found-logo" src="./assets/img/error_404.png">
  <div class="fx-not-found-text">Sorry, the page you are looking for does not exist.</div>
</div>
```

In this page, we display:

- an image with *404 Not found page* message
- an appropriate message

- **not-found-page.component.css**:

```CSS
.fx-not-found-container {
    background-color: rgb(141,213,170);
    padding-top: 120px;
    display: flex;
    align-items: center;
    flex-direction: column;
}

.fx-not-found-logo {
    margin-bottom: 40px;
    width: 395px;
}

.fx-not-found-text {
    font-size: 16px;
    color: #7C7C7C;
    margin-left: 60px;
}
```

For *not-found-page.component.ts*, we will let it as it was generated by *Angular CLI*, because we do not have specific logic for this page:

- **not-found-page.component.ts**:

```JavaScript
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-not-found-page',
  templateUrl: './not-found-page.component.html',
  styleUrls: ['./not-found-page.component.css']
})
export class NotFoundPageComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
```
