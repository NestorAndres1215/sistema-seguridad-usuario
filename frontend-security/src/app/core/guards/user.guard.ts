import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { GoogleService } from '../services/google.service';

@Injectable({
  providedIn: 'root'
})
export class UserGuard implements CanActivate {

  constructor(private authService: GoogleService, private router: Router) { }

  async canActivate(): Promise<boolean | UrlTree> {
    try {
      const token = this.authService.token;
      const user = await firstValueFrom(this.authService.getCurrentUser());
      const rol = user?.role?.name;

      if (token && this.authService.isLoggedIn()) {
        if (rol === 'ROLE_USER') {
          return true;
        } else if (rol === 'ROLE_ADMIN') {
          return this.router.parseUrl('/dashboard-admin');
        }
      }

      return this.router.parseUrl('/login');
    } catch (error) {
      return this.router.parseUrl('/login');
    }
  }
}
