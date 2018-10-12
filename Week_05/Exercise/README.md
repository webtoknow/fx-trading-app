# Week 5 - Create Login and Register pages with Angular

## Exercise 1 - Pages, Routing & Navigation

### Create pages

Go to *Week_05/Exercise/Code/ui*

````bash
cd fx-trading-app\Week_05\Exercise\Code\ui
````

Run *npm install* to download all dependencies

````bash
npm install
````

Create a folder for pages in *ui/src/app*

````bash
cd src\app
mkdir pages
````

Generate page components using CLI:

````bash
ng generate component dashboard-page
ng generate component login-page
ng generate component not-found-page
ng generate component register-page
````

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
Pass it to the RouterModule.forRoot method in the module imports to configure the router.

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

Add RouterOutlet directive to app *app.component.html* by removing the old markup.

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

### Update index.html,package.json and app.module by adding bootstrap, datepicker and fontawesome

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

- authService, running on port 8200
- fxTradeService, running on port 8210
- quoteService, running on port 8220

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

So, our service:

- will be marked as injectable through *@Injectable()* adnotation
- will contain a constructor, in which we will inject *HTTPClient* in order to be able to make HTTP requests
- will contain also the *register* method, receiving an *User* object - the one we want to save in order to create a new account - which will actually do the http request: a **POST** on the URL estabilished in *constants.ts* (*backendUrl.authService.register*) with *user* as *Request Body*

### Update app.module.ts

- include Alert module from bootstrap:

```JavaScript
import { AlertModule } from 'ngx-bootstrap/alert';

imports: [
    ...,
    AlertModule.forRoot(),
    ...
]
```

- add Forms module in order to use them:

```JavaScript
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

 imports: [
    ...,
    FormsModule,
    ReactiveFormsModule,
    ...
 ]
```

- include Http Client:

```JavaScript
import { HttpClientModule } from '@angular/common/http';

imports: [
    ...,
    HttpClientModule,
    ...
  ]
```

- include User Service:

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
- when we press on Register, the *onSubmit* function is triggered: *(ngSubmit)="onSubmit()"*
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
- we have a link to Login Page:

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
- the form, its fields and validations for them are specified in the class
- in *onSubmit* function, if the form is valid, we use *userService.register* to send the entity to be saved. We display a message even if it is a successful request or not.