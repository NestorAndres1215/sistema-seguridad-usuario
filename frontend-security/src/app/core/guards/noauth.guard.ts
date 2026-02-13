import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { firstValueFrom, Observable, of } from 'rxjs';
import { GoogleService } from '../services/google.service';


@Injectable({
  providedIn: 'root'
})
export class NoAuthGuard implements CanActivate {

  constructor(private authService: GoogleService, private router: Router) { }

  async canActivate(): Promise<boolean | UrlTree> {
    if (this.authService.isLoggedIn()) {
      try {
        const user = await firstValueFrom(this.authService.getCurrentUser());
        const role = user?.role?.name;

        // Definir destino según rol
        const destino: string =
          role === 'ROLE_ADMIN' ? '/dashboard-admin' :
          role === 'ROLE_USER' ? '/dashboard' :
          '/login';

        // Retornar UrlTree → Angular hace la navegación
        return this.router.parseUrl(destino);

      } catch (error) {
        console.error('Error obteniendo usuario:', error);
        return this.router.parseUrl('/login');
      }
    }

    // No está logueado → puede continuar
    return true;
  }
}