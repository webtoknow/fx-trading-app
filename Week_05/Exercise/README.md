# Week 5 - Create Login and Register pages with Angular

## Pages, Routing & Navigation

### Create pages

Got to *Week_05\Exercise\Code*

````bash
cd fx-trading-app\Week_05\Exercise\Code
````

Create a folder for pages in *ui\src\app*

````bash
cd ui\src\app
mkdir pages
````

Generate page components using CLI

````bash
ng generate component dashboard-page
ng generate component login-page
ng generate component not-found-page
ng generate component register-page
````

### Add rotes

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

Remove tilte from *app.component.ts*

```JS
title = 'ui';
```