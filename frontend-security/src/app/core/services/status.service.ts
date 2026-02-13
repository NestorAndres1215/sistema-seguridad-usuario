import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StatusService {
  private backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) { }

  getAllStatus(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/statuses/list`);
  }

  getStatusById(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/statuses/${id}`);
  }


}
