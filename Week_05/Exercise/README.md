# Week 5 - Create Login and Register pages with Angular

## Table of contents

- [Exercise 1 - Pages, Routing and Navigation](#exercise-1---pages-routing-and-navigation)
  - [Create pages](#create-pages)
  - [Add routes](#add-routes)
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
- [Exercise 4 - Simulate the backend server to check login and register](#exercise-4---simulate-the-backend-server-to-check-login-and-register)

## Exercise 1 - Pages, Routing and Navigation

### Create pages

Go to *Week_05/Exercise/Code/ui*

```bash
cd fx-trading-app\Week_05\Exercise\Code\ui
```

Run *npm install* to download all dependencies

```bash
npm install
```

Create a folder for pages in *ui/src/app*

```bash
cd src\app
mkdir pages
```

Generate page components using CLI:

```bash
ng generate component dashboard-page
ng generate component login-page
ng generate component not-found-page
ng generate component register-page
```

### Add routes

Import *RouterModule, Routes* from *@angular/router*

```JS
import { RouterModule, Routes } from '@angular/router';
```

Create routes by linking the components

```JS
const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'dashboard', component: DashboardPageComponent },
  { path: '**', component: NotFoundPageComponent }
];
```

The *appRoutes* array of routes describes how to navigate.
Pass it to the *RouterModule.forRoot* method in the module imports to configure the router.

```JS
{
  ...
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
  ],
  ...
}
```

Add *RouterOutlet* directive to app *app.component.html* by removing the old markup.

```HTML
<router-outlet></router-outlet>
```

Remove title from *app.component.ts*

```JS
title = 'ui';
```

### Fill in global style file

Open *styles.css* global style file and fill in with these classes, used in the whole application, not only in one specific component:

```CSS
/* You can add global styles to this file, and also import other style files */

html, body{
    height: 100%;
    color: #373A3C;
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
} 

.title-border {
    border-bottom: 1px solid #DDDDDD;
}

.screen-full-height {
    height: 100vh;
}

.ng-invalid:not(form)  {
  border-left: 5px solid #D9534F;
}

.clickable:hover {
    cursor: pointer;
    opacity: 0.8;
}

.alert {
    margin-bottom: 0;
}
.alert-container {
    text-align: center;
    height: 48px;
    margin-top: 10px;
}
```

### Add Bootstrap, Datepicker and Fontawesome

- *index.html*:

```HTML
 <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
 <link href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" rel="stylesheet" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
 <link rel="stylesheet" href="https://unpkg.com/ngx-bootstrap/datepicker/bs-datepicker.css">
```

- *package.json*:

```JSON
"dependencies": {
    ...,
    "ngx-bootstrap": "^3.0.1",
    ...
}
```

```bash
npm install
```

- *app.module.ts*:

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
    id: number;
    username: string;
    email: string
    password: string;
}
```

### Constants file

We will need to communicate with Backend through some API's. Let's estabilish how they will look like.

On server side, we will have 3 micro-services:

- *authService*, running on port 8200
- *fxTradeService*, running on port 8210
- *quoteService*, running on port 8220

Into *Week_05/Exercise/Code/ui/src/app*, we will create a new file, *contants.ts*, containing all API's needed:

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

- will be marked as injectable through *@Injectable()* adnotation
- will contain a constructor, in which we will inject *HTTPClient* in order to be able to make HTTP requests
- will contain also the *register* method, receiving an *User* object - the one we want to save in order to create a new account - which will actually do the HTTP request: a **POST** on the URL estabilished in *constants.ts* (*backendUrl.authService.register*) with *user* as *Request Body*

### Update Application Module

In app.module.ts:

- include *Aler*t module from bootstrap:

```JavaScript
import { AlertModule } from 'ngx-bootstrap/alert';

imports: [
    ...,
    AlertModule.forRoot(),
    ...
]
```

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
<div class="row screen-full-height">
  <div class="col-md-6 login-logo-container container-center">
    <img class="fx-grayscale-logo" alt="fx-logo" src="./assets/img/logo-grayscale.svg">
  </div>
  <div class="col-md-6">
    <div class="container-center screen-full-height">

      <div class="content">
        <div class="title title-border">
          <h4>Register a new account</h4>
        </div>
        <form id="register" [formGroup]="registerForm" (ngSubmit)="onSubmit()">
          <div class="form-group flex">
            <div class="col">
              <label for="username">Username</label>
              <input type="text" class="form-control form-control-sm" id="inputUsername" formControlName="username" placeholder="username"
                [ngClass]="{ 'is-invalid': submitted && f.username.errors }" />
              <span *ngIf="submitted && f.username.errors" class="invalid-feedback">
                <span *ngIf="f.username.errors.required">Username is required!</span>
              </span>
            </div>
          </div>
          <div class="form-group flex">
            <div class="col">
              <label for="email">Email</label>
              <input type="text" class="form-control form-control-sm" id="inputEmail" formControlName="email" placeholder="email address"
                [ngClass]="{ 'is-invalid': submitted && f.email.errors }" />
              <div *ngIf="submitted && f.email.errors" class="invalid-feedback">
                <div *ngIf="f.email.errors.required">Email is required!</div>
              </div>
            </div>
          </div>
          <div class="form-group flex">
            <div class="col">
              <label for="password">Password</label>
              <input type="password" class="form-control form-control-sm" id="inputPassword" formControlName="password" placeholder="password"
                [ngClass]="{ 'is-invalid': submitted && f.password.errors }" />
              <div *ngIf="submitted && f.password.errors" class="invalid-feedback">
                <div *ngIf="f.password.errors.required">Password is required!</div>
                <div *ngIf="f.password.errors.minlength">Password must be at least 6 characters!</div>
              </div>
            </div>
          </div>
          <div class="form-group flex">
            <div class="col">
              <label for="password">Confirm password</label>
              <input type="password" class="form-control form-control-sm" id="inputConfirmPassword" formControlName="confirmPassword" placeholder="confirm password"
                [ngClass]="{ 'is-invalid': submitted && f.confirmPassword.errors }" />
              <div *ngIf="submitted && f.confirmPassword.errors" class="invalid-feedback">
                <div *ngIf="f.confirmPassword.errors.required">Password confirmation is required!</div>
              </div>
            </div>
          </div>
          <button [disabled]="loading" type="submit" class="btn btn-primary btn-block">Register</button>
          <div class="text-container">
            <span>Already have an account?&nbsp;</span>
            <a [routerLink]="['/login']" class="btn btn-link">Login</a>
          </div>
        </form>
        <div class="alert-container">
            <alert *ngIf="isModalOpen" [type]="modalType" dismissOnTimeout="5000" (onClosed)="isModalOpen = false">{{modalMessage}}</alert>
          </div>
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
     <span *ngIf="submitted && f.username.errors" class="invalid-feedback">
        <span *ngIf="f.username.errors.required">Username is required!</span>
    </span>
    ```
  - minimum length validations:
    - *e.g.*:
    ```HTML
     <div *ngIf="submitted && f.password.errors" class="invalid-feedback">
       <div *ngIf="f.password.errors.minlength">Password must be at least 6 characters!
       </div>
    </div>
    ```
- we have a link to *Login Page*:

```HTML
<div class="text-container">
    <span>Already have an account?&nbsp;</span>
    <a [routerLink]="['/login']" class="btn btn-link">Login</a>
</div>
```

- if we have an error, an bootstrap alert is displayed (it will be hidden after 5 seconds):

```HTML
<div class="alert-container">
    <alert *ngIf="isModalOpen" [type]="modalType" dismissOnTimeout="5000" (onClosed)="isModalOpen = false">{{modalMessage}}</alert>
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
    background-color: rgb(141,213,170);
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

.flex {
    display: flex;
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

import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.css']
})
export class RegisterPageComponent implements OnInit {

  registerForm: FormGroup;
  loading = false;
  submitted = false;
  isModalOpen = false;
  modalMessage = '';
  modalType = '';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    })
  }

  //getter for easy access to form fields
  get f() { return this.registerForm.controls; }

  onSubmit() {
    this.submitted = true;

    //if form is invalid
    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;
    this.userService.register(this.registerForm.value)
      .pipe(first())
      .subscribe(
        data => {
          this.isModalOpen = true;
          this.modalType = 'success';
          this.modalMessage = 'Registration successful!';
        },
        error => {
          this.isModalOpen = true;
          this.modalType = 'danger';
          this.modalMessage = error;
          this.loading = false;
        }
      )
  }

}
```

We can notice here:

- the component is declared through *@Component* adnotation, by specifying the selector, template and style files
- the form, its fields and validations are specified in the class
- in *onSubmit* function, if the form is valid, we use *userService.register* to send the entity to be saved. We display a message even if it is a successful request or not.

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
        this.router.navigate(['/login'], { queryParams: { returnUrl: state.url }});
        return false;
    }

}
```

After creating the class, we need to put it on the private routes. So, in *app.module.ts*, we will update the routes declaration to be:

```JavaScript
import { AuthGuard } from 'src/app/guards/auth.guard';

const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full', canActivate: [AuthGuard] },
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

import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    intercept(request: HttpRequest<any>, next: HttpHandler) : Observable<HttpEvent<any>> {
        // add authorization header with jwt token if available
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
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
    constructor(private authenticationService: AuthenticationService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler) : Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(err => {
            if (err.status === 401) {
                //logout if 401 response is returned from api
                this.authenticationService.logout();
                location.reload(true);
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

Let's fill in the neccesary files for login functionality with some code in order to make these work:

- **login-page.component.html**:

```HTML
<div class="row screen-full-height">
  <div class="col-md-6 login-logo-container container-center">
    <img class="fx-grayscale-logo" alt="fx-grayscale-logo" src="./assets/img/logo-grayscale.svg">
  </div>
  <div class="col-md-6">
    <div class="container-center screen-full-height">
      <div class="content">
        <div class="title title-border">
          <h4>Login to your account</h4>
        </div>
        <form id="login" [formGroup]="loginForm" (ngSubmit)="onSubmit()">
          <div class="form-group flex">
            <i class="fa fa-user icon" aria-hidden="true"></i>
            <div class="col">
              <input type="text" class="form-control form-control-sm" id="inputUsername" formControlName="username" placeholder="username"
                [ngClass]="{ 'is-invalid': submitted && f.username.errors }" />
              <span *ngIf="submitted && f.username.errors" class="invalid-feedback">
                <span *ngIf="f.username.errors.required">Username is required!</span>
              </span>
            </div>
          </div>
          <div class="form-group flex">
            <i class="fa fa-lock icon" aria-hidden="true"></i>
            <div class="col">
              <input type="password" class="form-control form-control-sm" id="inputPassword" formControlName="password" placeholder="password"
                [ngClass]="{ 'is-invalid': submitted && f.password.errors }" />
              <div *ngIf="submitted && f.password.errors" class="invalid-feedback">
                <div *ngIf="f.password.errors.required">Password is required!</div>
              </div>
            </div>
          </div>
          <button [disabled]="loading" type="submit" class="btn btn-primary btn-block">Login</button>
          <div class="text-container">
            <span>You don't have an account?&nbsp;</span>
            <a [routerLink]="['/register']" class="btn btn-link">Register</a>
          </div>
        </form>
        <div class="alert-container">
          <alert *ngIf="isModalOpen" [type]="modalType" dismissOnTimeout="5000" (onClosed)="isModalOpen = false">{{modalMessage}}</alert>
        </div>
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

  <span *ngIf="submitted && f.username.errors" class="invalid-feedback">
    <span *ngIf="f.username.errors.required">Username is required!</span>
  </span>
  ```

- we have a link to *Register Page*:

```HTML
<div class="text-container">
    <span>You don't have an account?&nbsp;</span>
    <a [routerLink]="['/register']" class="btn btn-link">Register</a>
</div>
```

- if we have an error, an bootstrap alert is displayed (it will be hidden after 5 seconds):

```HTML
<div class="alert-container">
    <alert *ngIf="isModalOpen" [type]="modalType" dismissOnTimeout="5000" (onClosed)="isModalOpen = false">{{modalMessage}}</alert>
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

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  isModalOpen = false;
  modalMessage = '';
  modalType = '';
  returnUrl: string;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit() {

    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    this.authenticationService.logout();

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';

  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authenticationService.login(this.f.username.value, this.f.password.value)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate([this.returnUrl]);
        }, 
        error => {
          this.isModalOpen = true;
          this.modalType = 'danger';
          this.modalMessage = error;
          this.loading = false;
        }
      )
  }

}

```

We can observe:

- the component is declared through *@Component* adnotation
- the form, its fields and validations are specified in the class
- in *onSubmit* function, if the form is valid, we use *authenticationService.login* to send the username and password to the server. If the request is successful, we will be redirected to *Dashboard* page or the previous accessed page where we did not have access initially (*returnUrl*). Else, we will display an error message.

## Exercise 4 - Simulate the backend server to check login and register

At this moment, we do not have implemented a backend server. To check if our login and register functionalities are working, we can simulate it, by creating a new file named *fake-backend.ts* into *Week_05/Exercise/Code/ui/src/app/helpers*:

```JavaScript
import { Injectable } from '@angular/core';
import { HttpRequest, HttpResponse, HttpHandler, HttpEvent, HttpInterceptor, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { delay, mergeMap, materialize, dematerialize } from 'rxjs/operators';
 
@Injectable()
export class FakeBackendInterceptor implements HttpInterceptor {

    constructor() { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // array in local storage for registered users
        let users: any[] = JSON.parse(localStorage.getItem('users')) || [];

        // wrap in delayed observable to simulate server api call
        return of(null).pipe(mergeMap(() => {

            // authenticate
            if (request.url.endsWith('/authenticate') && request.method === 'POST') {
                // find if any user matches login credentials
                let filteredUsers = users.filter(user => {
                    return user.username === request.body.username && user.password === request.body.password;
                });

                if (filteredUsers.length) {
                    // if login details are valid return 200 OK with user details and fake jwt token
                    let user = filteredUsers[0];
                    let body = {
                        id: user.id,
                        username: user.username,
                        email: user.email,
                        token: 'fake-jwt-token'
                    };

                    return of(new HttpResponse({ status: 200, body: body }));
                } else {
                    // else return 400 bad request
                    return throwError({ error: { message: 'Username or password is incorrect!' } });
                }
            }

            // register user
            if (request.url.endsWith('/register') && request.method === 'POST') {
                // get new user object from post body
                let newUser = request.body;

                // validation
                let duplicateUser = users.filter(user => { return user.username === newUser.username; }).length;
                if (duplicateUser) {
                    return throwError({ error: { message: 'Username "' + newUser.username + '" is already taken!' } });
                }

                // save new user
                newUser.id = users.length + 1;
                users.push(newUser);
                localStorage.setItem('users', JSON.stringify(users));

                // respond 200 OK
                return of(new HttpResponse({ status: 200 }));
            }

            // pass through any requests not handled above
            return next.handle(request);

        }))

        // call materialize and dematerialize to ensure delay even if an error is thrown (https://github.com/Reactive-Extensions/RxJS/issues/648)
        .pipe(materialize())
        .pipe(delay(500))
        .pipe(dematerialize());
    }
}

export let fakeBackendProvider = {
    // use fake backend in place of Http service for backend-less development
    provide: HTTP_INTERCEPTORS,
    useClass: FakeBackendInterceptor,
    multi: true
};
```

So, in this class, we intercept all HTTP requests.
If the request url ends with */register*, we will simulate the register by adding to the existent *users* property from *localStorage* the new user, only if this does not duplicate some existent ones, and send the 200 status.

If the request url ends with */authenticate*, we will simulate the login by putting the token on the response, like the server will do.

Now, we have to include it into *app.module.ts*:

```JavaScript
import { fakeBackendProvider } from './helpers/fake-backend';

providers: [
    ...
    fakeBackendProvider
  ],
```