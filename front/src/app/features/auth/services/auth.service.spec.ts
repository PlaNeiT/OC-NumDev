import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

const mockSessionInformation: SessionInformation = {
  token: 'fake-token',
  type: 'Bearer',
  id: 1,
  username: 'yoga@studio.com',
  firstName: 'Admin',
  lastName: 'Admin',
  admin: true,
};

const mockRegisterRequest: RegisterRequest = {
  email: 'test@example.com',
  firstName: 'Test',
  lastName: 'User',
  password: 'password123',
};

const mockLoginRequest: LoginRequest = {
  email: 'test@example.com',
  password: 'password123',
};

describe('AuthService', () => {
  let service: AuthService;
  let httpClientSpy: { post: jest.Mock };

  beforeEach(() => {
    httpClientSpy = {
      post: jest.fn(),
    };

    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [
        { provide: HttpClient, useValue: httpClientSpy },
        AuthService,
      ],
    });

    service = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should register a user', () => {
    httpClientSpy.post.mockReturnValue(of(undefined));

    service.register(mockRegisterRequest).subscribe((response) => {
      expect(response).toBeUndefined();
    });

    expect(httpClientSpy.post).toHaveBeenCalledWith('api/auth/register', mockRegisterRequest);
  });

  it('should login a user and return session information', () => {
    httpClientSpy.post.mockReturnValue(of(mockSessionInformation));

    service.login(mockLoginRequest).subscribe((sessionInfo) => {
      expect(sessionInfo).toEqual(mockSessionInformation);
    });

    expect(httpClientSpy.post).toHaveBeenCalledWith('api/auth/login', mockLoginRequest);
  });
});
