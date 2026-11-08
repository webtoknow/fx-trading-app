import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  if (localStorage.getItem('currentUser')) {
    // we are logged in => return true
    return true;
  }

  // we are not logged in => redirect to login page with the return url
  return inject(Router).createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
};
