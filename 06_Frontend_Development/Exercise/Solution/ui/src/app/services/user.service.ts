import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { User } from '../models/user';
import { backendUrl } from '../constants';

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) { }

  // The backend answers POST /user/register with an empty body
  register(user: User): Observable<void> {
    return this.http.post<void>(backendUrl.authService.register, user);
  }

}
