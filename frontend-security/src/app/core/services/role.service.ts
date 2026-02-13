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
}
