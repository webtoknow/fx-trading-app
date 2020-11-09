# Week 6 - Create Dashboard page with Angular

## Table of contents

- [Observables](#observables)
  - [About](#about)
  - [Observables vs Promises](#observables-vs-promises)
  - [Pull vs Push](#pull-vs-push)
  - [Observable lifecycle](#observable-lifecycle)
- [Forms and Validations](#forms-and-validations)
  - [Reactive Forms](#reactive-forms)
  - [Template-driven Forms](#template-driven-forms)
  - [Key differences between Reactive and Template-driven forms](#key-differences-between-reactive-and-template-driven-forms)

## About Angular - part II

### Observables

#### About

- provide support for passing messages between **publishers** and **subscribers** in our application
- offer signifiant benefits over other techinques for event handling, asynchronous programming and handling multiple values
- are declarative, meaning that we define a function for publishing values, but it is not executed until a consumer subscribes to it
- the subscribed consumer then receives notifications until the function completes or until they unsubscribe
- to be able to use observables, we need to do the following steps:
  - to create an observable, we have to import **rxjs/observable**, so we will be able to create and work with Observable type, needed until this will become part of the language:

  ```JavaScript
  import { Observable } from 'rxjs';
  ```

  - then, we have to subscribe to the Observable in the application which will allow us to listen to the data that is coming along with it:

  ```JavaScript
   getCurrencies() {
        return this.http.get(backendUrl.quoteService.getCurrencies) as Observable<string[]>
    }
  ```

  ```JavaScript
  this.tradeService.getCurrencies().subscribe((response) => {
      this.currencies = response;
    })
  ```

#### Observables vs Promises

- using **Promises**, we make a request and waiting for a **single response** - it will not be multiple responses on same request
- attempting to resolve same Promise again with an another value will fail - it is always resolved with the first value and ignore the future ones
- **Observables** allow to resolve (better saying, **emit**) multiple values

```JavaScript
const observable = mew Observable((observer) => {
    let i=0;
    setInterval(() => {
        observer.next(i++);
    },1000);
}

observable.subscribe(value => console.log(value));

// logs:
// 0
// 1
// 2
// and so on, every second
```

- **Promises are eager**, **Observables are lazy** : function passed to Observable constructor gets called only when someone actually subscribes to an Observable - not wasting resources
- Promises are **not cancellable**, **Observables are cancelable**, so we can stop whatever happens inside it

```JavaScript
const subscription = observable.subscribe(value => console.log(value));

subscription.unsubscribe();
```

#### Pull vs Push

- two different ways to describe how the **data consumer** communicates with the **data producer**
- **Pull**:
  - data consumer is the part that decides when it get's data from the producer - this happen while pulling
  - the time it takes the data to be delivered to the data consumer is unknown even for the data producer
  - *e.g.:*
    - *Data producers: JavaScript Functions*
    - *Data consumers: code that calls the previous functions*
- **Push**:
  - the common way to push in JS are **promises**
  - we can consider the Promises as producer, but this time it delivers a value to registered callbacks
  - **Observables** are the new way to push data in JS

#### Observable lifecycle

- Observables can be:
  - **Created** by using the **new Observable()** call
  - **Subscribed** to by an **observer**
  - **Executed** by calling the **next()**
  - **Disposed** by calling **unsubscribe()**

  **Creating observable**:
  - just call **new Observable()** and pass one argument (which is the observer here)

  **Subscribing to observables**:
  - if you don't subscribe to observables, nothing will happen, because they are a lazy collection of data or multiple values over time

  **Executing observables**:
  - the execution of the observables is the code inside it
  - there are 3 functions used to send data to observables:
    - **next**: it sends any value such as Arrays, objects or Numbers to it's subscribers
    - **complete**: it doesn't send any value
    - **error**: it sends a JS exception or error
  - the **next** calls are the most common because they deliver the data to subscribers

  **Disposing observables**:
  - because the time it takes to execute can be infinite amount of time, we need a way to stop it when we want to
  - we will need to unsubscribe from the observable to do cleanup and release resources as it will be a waste of memory and computing power

### Forms and Validations

- an Angular Form needs a class for logic and a template for the user
- forms have to block invalid data to be sent
- validations on client side guide the user and avoid useless requests
- there are 2 ways of creating Angular forms: **Template-driven** and **Reactive (model-driven)**
- none of these is wrong
- both are using **FormGroup** and **FormControl** building blocks

### Reactive forms

- are more robust, scalable, reusable and testable
- best practice is to be used when forms are a key part of the application
- the form model provides the value and status of the form element at a given point of time: **model-driven approach**
- each form element in the view is directly linked to a form model (*FormControl* instance)
- updates from the view to the model and from the model to the view are synchronous and aren't dependent on the UI rendered
- validators are not added through attributes in the template
- validator functions are added directly to the form control model in the component class
- *e.g.*:

    ```javascript
    const formGroup = new FormGroup({
        'phones': new FormArray([
            new FormGroup({
                'number': new FormControl('', [ Validators.required ]),
                'type': new FormControl('Primary')
            }),
            new FormGroup({
                'number': new FormControl(''),
                'type': new FormControl('Secondary')
            })
        ])
    });
    ```

    ```HTML
    <form [formGroup]="formGroup" >
        <h1>User Phones</h1>
        <div class="phones"
            *ngFor="let phoneGroup of formGroup.get('phones')['controls'];
                    let i = index"
            formArrayName="phones">
            <ng-container [formGroupName]="i" >
            <p>Phone Type: {{ phoneGroup.get('type').value }}</p>
            <input
                type="tel"
                placeholder="Phone number"
                formControlName="number"
            />
            </ng-container>
        </div>
    </form>
    ```

> Note
>
> We can also create our own Custom Validators.

### Template-driven forms

- easy to be added in an app
- don't scale as well as reactive forms
- best practice is to be used if form requirements are basic and logic can be managed only in the template
- the template provides the value and status of the form element at a given point of time:**template-driven approach**
- each element is linked to a directive that manages the form model internally
- to add validation, we need to add the same validation attributes and for native HTML form validation
- every time the value of a form control changes, Angular runs validation and generates a list of validation errors, which results in an *INVALID* status, or null, which results in a *VALID* status
- *e.g.*:

    ```HTML
    <input id="name" name="name" class="form-control" required minlength="4" [(ngModel="book.name")]>
    <div *ngIf="name.invalid && (name.dirty || name.touched)" class="alert alert-danger">
        <div *ngIf="name.errors.required">Name is required.</div>
        <div *ngIf="name.errors.minlength">Name must be at least 4 characters long.</div>
    </div>
    ```

> Note
>
> We can also create our own Custom Validators.

### Key differences between Reactive and Template-driven forms

| Key | Reactive | Template-driven |
| ------------- | ------------- | ------------- |
| Setup (form model) | More explicit, created in component class  | Less explicit, created by directives  |
| Data model | Structured | Unstructured |
| Predictability | Synchronous | Asynchronous |
| Form validation | Functions | Directives |
| Mutability | Immutable | Mutable |
| Scalability | Low-level API access | Abstraction on top of APIs |
