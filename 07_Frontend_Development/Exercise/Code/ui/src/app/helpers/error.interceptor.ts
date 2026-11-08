// intercepts http responses from the api to check if there were any errors.
// If there is a 401 Unauthorized response the user is automatically logged out of the application.

import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthenticationService } from '../services/authentication.service';

export const errorInterceptor: HttpInterceptorFn = (request, next) => {
  const authenticationService = inject(AuthenticationService);

  return next(request).pipe(catchError((err: HttpErrorResponse) => {
    if (err.status === 401) {
      //logout if 401 response is returned from api
      authenticationService.logout();
    }

    return throwError(() => err);
  }));
};
