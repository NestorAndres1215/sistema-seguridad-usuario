import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { GoogleService } from '../services/google.service';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(
    private authService: GoogleService,
    private router: Router,
  ) {}

  async canActivate(): Promise<boolean | UrlTree> {
    try {
      const user = await firstValueFrom(this.authService.getCurrentUser());

      const rol = user?.role?.name;

      if (rol === 'ROLE_ADMIN') {
        return true;
      }

      if (rol === 'ROLE_USER') {
        return this.router.parseUrl('/dashboard');
      }

      return this.router.parseUrl('/login');
    } catch (error) {
      console.error('Usuario no autenticado:', error);

      return this.router.parseUrl('/login');
    }
  }
}
