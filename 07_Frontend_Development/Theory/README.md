# Frontend Development: Observables and Forms

![You are here](../frontend.png)

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

- Observables are a fundamental concept in Angular that provides support for passing messages between publishers and subscribers in an application.
- They offer significant benefits over other techniques for event handling, asynchronous programming, and handling multiple values.
- Observables are declarative, meaning that we define a function for publishing values, but it is not executed until a consumer subscribes to it.
- Subscribed consumers receive notifications until the function completes or until they unsubscribe.
- To use observables in Angular, follow these steps:
  - Import the **Observable** type from 'rxjs':

  ```JavaScript
  import { Observable } from 'rxjs';
  ```

  - Subscribe to the observable in your application to listen to the data it emits:

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
Async pipe:
- The **async** pipe is a powerful feature in Angular for working with observables.
- It simplifies the process of binding observable data directly to the template.
```
@Component({
  selector: 'async-observable-pipe',
  template: '<div><code>observable|async</code>: Time: {{ time | async }}</div>'
})
export class AsyncObservablePipeComponent {
  time = new Observable<string>((observer: Observer<string>) => {
    setInterval(() => observer.next(new Date().toString()), 1000);
  });
}
```

#### Observables vs Promises

- **Observables** differ from **Promises** in several ways:
  - Observables allow for emitting multiple values over time, while Promises handle a single response.
  - Promises are eager and always return the first value, while Observables are lazy and execute only when someone subscribes.
  - Observables are cancelable, allowing you to stop them when needed.

```JavaScript
const observable = new Observable<number>((observer) => {
    let i = 0;
    const timer = setInterval(() => {
        observer.next(i++);
    }, 1000);
    return () => clearInterval(timer);
});

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

- Pull and push are two ways to describe how data **consumers** communicate with data **producers**.
- **Pull** is when the consumer decides when to get data from the producer.
  - the time it takes the data to be delivered to the data consumer is unknown even for the data producer
  - *e.g.:*
    - *Data producers: JavaScript Functions*
    - *Data consumers: code that calls the previous functions*
- **Push**, supported by Observables, is when the producer delivers data to registered callbacks.
  - the common way to push in JS are **promises**
  - we can consider the Promise as producer, but this time it delivers a value to registered callbacks
  - **Observables** are the new way to push data in JS

#### Observable lifecycle

- Observables go through a lifecycle:
  - **Created** by calling **new Observable()**.
  - **Subscribed** to by an **observer**.
  - **Executed** by calling **next()** to send data.
  - **Disposed** of by calling **unsubscribe()** when no longer needed.

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

### RxJS operators

- Operators let you transform, filter, combine, and control the timing of observable streams without mutating the source.
- Commonly used categories:
  - **Creation**: `of`, `from`, `interval`, `timer`, `throwError`.
  - **Transformation**: `map`, `mergeMap`, `switchMap`, `concatMap`, `exhaustMap`, `scan`.
  - **Filtering**: `filter`, `take`, `takeUntil`, `debounceTime`, `distinctUntilChanged`.
  - **Combination**: `forkJoin`, `combineLatest`, `withLatestFrom`, `merge`, `concat`, `zip`.
  - **Error handling**: `catchError`, `retry`, `retryWhen`.
  - **Multicasting/Sharing**: `shareReplay`, `share`, `publish`, `refCount`.
- Visual aid: http://rxmarbles.com/ is an excellent way to see how many core operators behave over time.

Examples:

```ts
import { of, interval, takeUntil, map, switchMap, filter, take } from 'rxjs';

// of: emit fixed values and complete
of(1, 2, 3).subscribe(v => console.log('of', v));
// logs instantly: of 1, of 2, of 3

// filter + take: only even values, stop after first 3 even numbers
interval(500).pipe(
  filter(n => n % 2 === 0),
  take(3)
).subscribe(v => console.log('even', v));
// timing: every 500ms
// logs: even 0, even 2, even 4, then completes

// takeUntil: stop when a notifier emits
const stop$ = interval(2000).pipe(take(1));
interval(300).pipe(
  takeUntil(stop$)
).subscribe(v => console.log('until stop', v));
// timing: every 300ms, stops at ~2000ms when stop$ emits its first value
// logs: until stop 0, 1, 2, 3, 4, 5 (then completes)

// map + switchMap: map a value, then switch to inner observable
of('USD').pipe(
  map(ccy => ccy + '/EUR'),
  switchMap(pair => interval(1000).pipe(take(2), map(i => `${pair}-${i}`)))
).subscribe(v => console.log('pair', v));
// logs over time: pair USD/EUR-0, pair USD/EUR-1 (then completes)
```

### Forms and Validations

- Angular forms are essential for creating user interfaces, blocking invalid data, guiding users, and avoiding unnecessary requests.
- There are two ways to create Angular forms: Template-driven and Reactive (model-driven), both using **FormGroup** and **FormControl** building blocks.
- Reactive forms are recommended for complex applications, as they provide a more robust, scalable, reusable, and testable solution.

### Reactive forms

- In reactive forms, the form model provides the value and status of form elements.
- Each form element in the view is directly linked to a **FormControl** instance in the model.
- Updates from the view to the model and vice versa are synchronous and independent of UI rendering.
- Validator functions are added directly to form control models in the component class.
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

- Template-driven forms are easier to use but may not scale well for complex requirements.
- The template manages the form model internally.
- Validation is added through attributes in the template, and validation is performed as the value of a form control changes.
- *e.g.*:

    ```HTML
    <input id="name" name="name" class="form-control" required minlength="4" [(ngModel="book.name")]>
    <div *ngIf="name.invalid && (name.dirty || name.touched)" class="alert alert-danger">
        <div *ngIf="name.errors?.required">Name is required.</div>
        <div *ngIf="name.errors?.minlength">Name must be at least 4 characters long.</div>
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
