import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of, Subject, tap } from 'rxjs';
import { environment } from '../../../environments/environments';
import { LoginAuth } from '../../models/loginAuth';

@Injectable({
  providedIn: 'root',
})
export class GoogleService {
  public loginStatusSubject = new Subject<boolean>();

  private readonly backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) {}

  loginWithCode(code: string): Observable<LoginAuth> {
    return this.http.post<LoginAuth>(
      `${this.backendUrl}/google/loginWithGoogle`,
      {
        code,
      },
      {
        withCredentials: true,
      },
    );
  }

  login(): void {
    this.http
      .get<{ url: string }>(`${this.backendUrl}/google/login-url`, {
        withCredentials: true,
      })
      .subscribe({
        next: (response) => {
          window.location.href = response.url;
        },

        error: (error) => {
          console.error('Error obteniendo URL Google:', error);
        },
      });
  }

  handleAuthCallback(code: string): Observable<any> {
    return this.http.post(
      `${this.backendUrl}/callback`,
      {
        code,
      },
      {
        withCredentials: true,
      },
    );
  }

  generateToken(loginData: any): Observable<any> {
    return this.http.post(`${this.backendUrl}/auth/generate-token`, loginData, {
      withCredentials: true,
    });
  }

  getCurrentUser(): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/auth/actual-usuario`, {
      withCredentials: true,
    });
  }

  logout(): Observable<any> {
    return this.http
      .post(
        `${this.backendUrl}/auth/logout`,
        {},
        {
          withCredentials: true,
          responseType: 'text' as 'json',
        },
      )
      .pipe(
        tap(() => {
          this.loginStatusSubject.next(false);
        }),

        catchError((error) => {
          console.error('Error cerrando sesión:', error);

          this.loginStatusSubject.next(false);

          return of(null);
        }),
      );
  }

  isLoggedIn(): Observable<boolean> {
    return this.getCurrentUser().pipe(
      tap(() => {
        this.loginStatusSubject.next(true);
      }),

      catchError(() => {
        this.loginStatusSubject.next(false);

        return of(false);
      }),
    );
  }
}
