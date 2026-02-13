import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';
import { Registrar } from '../../models/registrar';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) { }


  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users`);
  }

  createUser(request: Registrar): Observable<any> {
    return this.http.post(`${this.backendUrl}/users`, request, {
      responseType: 'text'
    });
  }


  getUserById(id: number): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/users/${id}`);
  }


  getUserByUsername(username: string): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/users/username/${username}`);
  }

  getUserByEmail(email: string): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/users/email/${email}`);
  }


  updateUser(userId: number, updatedUser: Registrar): Observable<any> {
    return this.http.put<any>(`${this.backendUrl}/users/${userId}`, updatedUser);
  }

  getUsersActive(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/status-active`);
  }
  getUsersInactive(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/status-inactive`);
  }
  getUsersBlocked(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/status-blocked`);
  }

  getUsersSuspend(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/status-suspend`);
  }

  getUsersByRoleUser(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-user`);
  }

  getUsersByRoleAdmin(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-admin`);
  }

  getActiveUsersByRoleUser(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-user/active`);
  }

  getSuspendedUsersByRoleUser(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-user/suspend`);
  }

  getInactiveUsersByRoleUser(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-user/inactive`);
  }

  getBlockedUsersByRoleUser(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-user/blocked`);
  }

  getActiveUsersByRoleAdmin(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-admin/active`);
  }

  getSuspendedUsersByRoleAdmin(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-admin/suspend`);
  }

  getInactiveUsersByRoleAdmin(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-admin/inactive`);
  }

  getBlockedUsersByRoleAdmin(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-admin/blocked`);
  }

  inactivarUsuario(userId: number): Observable<any> {
    return this.http.put(`${this.backendUrl}/users/inactive/${userId}`, {});
  }

  activarUsuario(userId: number): Observable<any> {
    return this.http.put(`${this.backendUrl}/users/active/${userId}`, {});
  }

  suspenderUsuario(userId: number): Observable<any> {
    return this.http.put(`${this.backendUrl}/users/suspend/${userId}`, {});
  }

  blockedUsuario(userId: number): Observable<any> {
    return this.http.put(`${this.backendUrl}/users/blocked/${userId}`, {});
  }

  getUserStatusPercentages() {
    return this.http.get<any[]>(`${this.backendUrl}/users/status-percentages`);
  }

}
