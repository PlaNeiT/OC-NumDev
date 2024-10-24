import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in and set session information', () => {
    const user: SessionInformation = {
      token: 'sample-token',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
    };

    service.logIn(user);
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(user);
  });

  it('should log out and clear session information', () => {
    const user: SessionInformation = {
      token: 'sample-token',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
    };

    service.logIn(user);
    service.logOut();
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should return an observable of the login status', (done) => {
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(false);
      service.logIn({
        token: 'sample-token',
        type: 'Bearer',
        id: 1,
        username: 'testuser',
        firstName: 'Test',
        lastName: 'User',
        admin: false,
      });
      service.$isLogged().subscribe(isLoggedAfterLogin => {
        expect(isLoggedAfterLogin).toBe(true);
        done();
      });
    });
  });
});
