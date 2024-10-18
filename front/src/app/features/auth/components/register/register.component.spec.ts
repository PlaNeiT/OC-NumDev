import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    const authServiceMock = {
      register: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: { navigate: jest.fn() } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should submit the form and redirect to login on success', () => {
    const registerRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    };

    jest.spyOn(authService, 'register').mockReturnValue(of(undefined));

    component.form.setValue(registerRequest);
    component.submit();

    expect(authService.register).toHaveBeenCalledWith(registerRequest);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should set onError to true on registration failure', () => {
    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error('Registration failed')));

    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });

    component.submit();

    expect(component.onError).toBe(true);
  });

  it('should mark email as invalid if empty', () => {
    const emailControl = component.form.controls['email'];
    emailControl.setValue('');

    expect(emailControl.invalid).toBeTruthy();
    expect(emailControl.errors?.['required']).toBeTruthy();
  });

  it('should mark firstName as invalid if empty', () => {
    const firstNameControl = component.form.controls['firstName'];
    firstNameControl.setValue('');

    expect(firstNameControl.invalid).toBeTruthy();
    expect(firstNameControl.errors?.['required']).toBeTruthy();
  });

  it('should mark lastName as invalid if empty', () => {
    const lastNameControl = component.form.controls['lastName'];
    lastNameControl.setValue('');

    expect(lastNameControl.invalid).toBeTruthy();
    expect(lastNameControl.errors?.['required']).toBeTruthy();
  });

  it('should mark password as invalid if empty', () => {
    const passwordControl = component.form.controls['password'];
    passwordControl.setValue('');

    expect(passwordControl.invalid).toBeTruthy();
    expect(passwordControl.errors?.['required']).toBeTruthy();
  });
});
