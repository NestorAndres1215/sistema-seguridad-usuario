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

  // =========================
  // Listar todos los usuarios
  // =========================
  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users`);
  }

  // =========================
  // Crear un usuario
  // =========================
  createUser(request: Registrar): Observable<any> {
    return this.http.post(`${this.backendUrl}/users`, request, {
      responseType: 'text'
    });
  }

  // =========================
  // Buscar usuario por ID
  // =========================
  getUserById(id: number): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/users/${id}`);
  }

  // =========================
  // Buscar usuario por username
  // =========================
  getUserByUsername(username: string): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/users/username/${username}`);
  }

  // =========================
  // Buscar usuario por email
  // =========================
  getUserByEmail(email: string): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/users/email/${email}`);
  }

  // =========================
  // Actualizar usuario
  // =========================
  updateUser(userId: number, updatedUser: Registrar): Observable<any> {
    return this.http.put<any>(`${this.backendUrl}/users/${userId}`, updatedUser);
  }
  // =========================
  // Listar usuarios por estados
  // =========================
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
  // =========================
  // Listar usuarios por rol
  // =========================
  getUsersByRoleUser(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-user`);
  }

  getUsersByRoleAdmin(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/users/role-admin`);
  }

  // ------------------ ROLE_USER ------------------
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

  // ------------------ ROLE_ADMIN ------------------
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


  // 🔹 Inactivar usuario
  inactivarUsuario(userId: number): Observable<any> {
    return this.http.put(`${this.backendUrl}/users/inactive/${userId}`, {});
  }

  // 🔹 Activar usuario
  activarUsuario(userId: number): Observable<any> {
    return this.http.put(`${this.backendUrl}/users/active/${userId}`, {});
  }

  // 🔹 Suspend usuario
  suspenderUsuario(userId: number): Observable<any> {
    return this.http.put(`${this.backendUrl}/users/suspend/${userId}`, {});
  }
  // 🔹 Blocked usuario
  blockedUsuario(userId: number): Observable<any> {
    return this.http.put(`${this.backendUrl}/users/blocked/${userId}`, {});
  }


  getUserStatusPercentages() {
    return this.http.get<any[]>(`${this.backendUrl}/users/status-percentages`);
  }

}
