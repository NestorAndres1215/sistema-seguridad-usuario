import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) { }


  generateToken(loginData: any) {
    return this.http.post(`${this.backendUrl}/auth/generate-token`, loginData);
  }

  getCurrentUser() {
    const token = localStorage.getItem('jwt');
    console.log(token)
    if (token) {
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      console.log(headers)
      return this.http.get(`${this.backendUrl}/auth/actual-usuario`, { headers });
    } else {

      return this.http.get(`${this.backendUrl}/auth/actual-usuario`);
    }
  }

  get token(): string | null {
    return localStorage.getItem('jwt');
  }

  setToken(token: string) {
    localStorage.setItem('jwt', token);
  }

  logout(): Observable<any> {
    const token = localStorage.getItem('jwt');

    if (!token) {
      return of({ message: 'No token to logout' });
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post(`${this.backendUrl}/auth/logout`, {}, { headers, responseType: 'text' as 'json' }).pipe(
      tap(response => {

        localStorage.removeItem('jwt');
      }),
      catchError(error => {
        return of(error);
      })
    );
  }

}
