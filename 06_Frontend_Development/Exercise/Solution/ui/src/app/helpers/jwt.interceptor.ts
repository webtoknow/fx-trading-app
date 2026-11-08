// intercepts http requests from the application to add a JWT auth token to the Authentication header
// if the user is logged in

import { HttpInterceptorFn } from '@angular/common/http';

export const jwtInterceptor: HttpInterceptorFn = (request, next) => {
  // add authorization header with jwt token if available
  const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
  if (currentUser && currentUser.token) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${currentUser.token}`
      }
    });
  }

  return next(request);
};
