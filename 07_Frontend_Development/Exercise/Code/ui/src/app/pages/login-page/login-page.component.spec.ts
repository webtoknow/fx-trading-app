import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideRouter } from '@angular/router';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { UserService } from '../../services/user.service';
import { AuthenticationService } from '../../services/authentication.service';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginPageComponent } from './login-page.component';

describe('LoginPageComponent', () => {
  let component: LoginPageComponent;
  let fixture: ComponentFixture<LoginPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginPageComponent, FormsModule, ReactiveFormsModule, ToastrModule.forRoot(), BsDatepickerModule],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([]), UserService, AuthenticationService],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  function submit(status: number, body: object, statusText: string) {
    const toastr = TestBed.inject(ToastrService);
    const errorSpy = spyOn(toastr, 'error');
    component.loginForm.setValue({ username: 'ana', password: 'secret' });
    component.onSubmit();
    expect(component.loading()).toBeTrue();
    TestBed.inject(HttpTestingController)
      .expectOne((r) => r.method === 'POST')
      .flush(body, { status, statusText });
    return errorSpy;
  }

  it('shows "incorrect" on a 401 and re-enables the button', () => {
    const errorSpy = submit(401, { message: 'Bad credentials' }, 'Unauthorized');
    expect(errorSpy).toHaveBeenCalledWith('Username or password is incorrect');
    expect(component.loading()).toBeFalse();
  });

  it('shows the server message on any other error', () => {
    const errorSpy = submit(500, { message: 'Boom' }, 'Server Error');
    expect(errorSpy).toHaveBeenCalledWith('Boom');
    expect(component.loading()).toBeFalse();
  });
});
