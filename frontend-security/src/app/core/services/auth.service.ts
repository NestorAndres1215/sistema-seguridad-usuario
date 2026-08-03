import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly backendUrl = environment.backendUrl;
  private readonly http = inject(HttpClient);

  /**
   * Login usuario/password
   * Backend crea cookie JWT
   */
  generateToken(loginData: any): Observable<any> {
    return this.http.post(`${this.backendUrl}/auth/generate-token`, loginData, {
      withCredentials: true,
    });
  }

  /**
   * Obtener usuario actual
   * Backend lee cookie JWT
   */
  getCurrentUser(): Observable<any> {
    return this.http.get(`${this.backendUrl}/auth/actual-usuario`, {
      withCredentials: true,
    });
  }

  /**
   * Logout
   * Backend invalida token y elimina cookie
   */
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
          console.log('Sesión cerrada correctamente');
        }),

        catchError((error) => {
          console.error('Error cerrando sesión:', error);

          return of(error);
        }),
      );
  }
}
