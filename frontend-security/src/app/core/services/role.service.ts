import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) { }

  getAllRole(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/role/list`);
  }
  getRoleById(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/roles/${id}`);
  }
  createRole(role: any): Observable<any> {
    return this.http.post<any>(`${this.backendUrl}/roles`, role);
  }
  deleteRole(id: number): Observable<void> {
    return this.http.delete<void>(`${this.backendUrl}/roles/${id}`);
  }
}
