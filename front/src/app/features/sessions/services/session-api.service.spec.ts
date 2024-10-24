import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

const mockSession: Session = {
  id: 1,
  name: 'Yoga Session',
  description: 'A relaxing yoga session.',
  date: new Date(),
  teacher_id: 2,
  users: [1, 2],
  createdAt: new Date(),
  updatedAt: new Date(),
};

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpClientSpy: { get: jest.Mock; post: jest.Mock; put: jest.Mock; delete: jest.Mock };

  beforeEach(() => {
    httpClientSpy = {
      get: jest.fn(),
      post: jest.fn(),
      put: jest.fn(),
      delete: jest.fn(),
    };

    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [
        { provide: HttpClient, useValue: httpClientSpy },
        SessionApiService,
      ],
    });

    service = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return all sessions', () => {
    httpClientSpy.get.mockReturnValue(of([mockSession]));

    service.all().subscribe((sessions) => {
      expect(sessions).toEqual([mockSession]);
    });

    expect(httpClientSpy.get).toHaveBeenCalledWith('api/session');
  });

  it('should return session details', () => {
    httpClientSpy.get.mockReturnValue(of(mockSession));

    service.detail('1').subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    expect(httpClientSpy.get).toHaveBeenCalledWith('api/session/1');
  });

  it('should delete a session', () => {
    httpClientSpy.delete.mockReturnValue(of({}));

    service.delete('1').subscribe((response) => {
      expect(response).toEqual({});
    });

    expect(httpClientSpy.delete).toHaveBeenCalledWith('api/session/1');
  });

  it('should create a session', () => {
    httpClientSpy.post.mockReturnValue(of(mockSession));

    service.create(mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    expect(httpClientSpy.post).toHaveBeenCalledWith('api/session', mockSession);
  });

  it('should update a session', () => {
    httpClientSpy.put.mockReturnValue(of(mockSession));

    service.update('1', mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    expect(httpClientSpy.put).toHaveBeenCalledWith('api/session/1', mockSession);
  });

  it('should participate in a session', () => {
    httpClientSpy.post.mockReturnValue(of(undefined));

    service.participate('1', 'userId').subscribe((response) => {
      expect(response).toBeUndefined();
    });

    expect(httpClientSpy.post).toHaveBeenCalledWith('api/session/1/participate/userId', null);
  });

  it('should un-participate from a session', () => {
    httpClientSpy.delete.mockReturnValue(of(undefined));

    service.unParticipate('1', 'userId').subscribe((response) => {
      expect(response).toBeUndefined();
    });

    expect(httpClientSpy.delete).toHaveBeenCalledWith('api/session/1/participate/userId');
  });
});
