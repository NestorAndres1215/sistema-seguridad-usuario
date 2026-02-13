import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of, Subject, tap, throwError } from 'rxjs';
import { environment } from '../../../environments/environments';
import { LoginAuth } from '../../models/loginAuth';

@Injectable({
  providedIn: 'root'
})
export class GoogleService {
  
  public loginStatusSubjec = new Subject<boolean>();

  private backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) { }

  loginWithCode(code: string): Observable<LoginAuth> {
    return this.http.post<LoginAuth>(
      `${this.backendUrl}/google/loginWithGoogle`,
      { code },
      { headers: { 'Content-Type': 'application/json' } }
    );
  }



  login() {
    this.http.get<{ url: string }>(`${this.backendUrl}/google/login-url`).subscribe({
      next: (response) => {
        console.log(response)
        window.location.href = response.url; 
      },
      error: (err) => {
        console.error('Error obteniendo URL de login', err);
      },
    });
  }

  handleAuthCallback(code: string) {
    return this.http.post<{ jwt: string }>(`${this.backendUrl}/callback`, {
      code,
    });
  }


  generateToken(loginData: any) {
    return this.http.post(`${this.backendUrl}/generate-token`, loginData);
  }

  getCurrentUser() {
    const token = localStorage.getItem('jwt');
    if (!token) {
      return throwError(() => new Error('No hay token disponible'));
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any>(`${this.backendUrl}/auth/actual-usuario`, { headers });
  }


  get token(): string | null {
    return localStorage.getItem('jwt');
  }

 
  setToken(token: string) {
    localStorage.setItem('jwt', token);
  }

  logout(): Observable<any> {
    const token = localStorage.getItem('jwt');
    localStorage.removeItem('jwt');
    if (!token) {
      return of({ message: 'No token to logout' });
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    
    return this.http.post(`${this.backendUrl}/auth/logout`, {}, { headers, responseType: 'text' as 'json' }).pipe(
      tap(response => {
        localStorage.removeItem('username')
        localStorage.removeItem('jwt');
      }),
      catchError(error => {
        return of(error);
      })
    );
  }

  isLoggedIn() {
    let tokenStr = localStorage.getItem('jwt');
    if (tokenStr == undefined || tokenStr == '' || tokenStr == null) {
      return false;
    } else {
      return true;
    }
  }

}
