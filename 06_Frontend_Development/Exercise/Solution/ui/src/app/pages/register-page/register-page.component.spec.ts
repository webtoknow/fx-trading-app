import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, provideRouter } from '@angular/router';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { UserService } from '../../services/user.service';
import { AuthenticationService } from '../../services/authentication.service';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { backendUrl } from '../../constants';

import { RegisterPageComponent } from './register-page.component';

describe('RegisterPageComponent', () => {
  let component: RegisterPageComponent;
  let fixture: ComponentFixture<RegisterPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterPageComponent, FormsModule, ReactiveFormsModule, ToastrModule.forRoot(), BsDatepickerModule],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([]), UserService, AuthenticationService],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('rejects mismatched passwords without sending registration', () => {
    component.registerForm.setValue({ username: 'ana', email: 'ana@example.com', password: 'password123', confirmPassword: 'different' });
    component.onSubmit();
    expect(component.f['confirmPassword'].hasError('mustMatch')).toBeTrue();
    TestBed.inject(HttpTestingController).expectNone(backendUrl.authService.register);
  });

  it('accepts the empty registration response from the Java backend', () => {
    const navigate = spyOn(TestBed.inject(Router), 'navigate');
    spyOn(TestBed.inject(ToastrService), 'success');
    component.registerForm.setValue({ username: 'ana', email: 'ana@example.com', password: 'password123', confirmPassword: 'password123' });
    component.onSubmit();
    expect(component.loading()).toBeTrue();
    TestBed.inject(HttpTestingController).expectOne(backendUrl.authService.register).flush(null);
    expect(component.loading()).toBeFalse();
    expect(navigate).toHaveBeenCalledWith(['/login']);
  });

});
