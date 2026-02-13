import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { GoogleService } from '../services/google.service';

@Injectable({
  providedIn: 'root'
})
export class UserGuard implements CanActivate {

  constructor(private authService: GoogleService, private router: Router) {}

  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean | UrlTree> {
    try {
      const token = this.authService.token;
      const user = await firstValueFrom(this.authService.getCurrentUser());
      const rol = user?.role?.name;

      console.log("🛡️ UserGuard → Usuario:", user);

      if (token && this.authService.isLoggedIn()) {
        if (rol === 'ROLE_USER') {
          console.log("✅ Acceso permitido: USER");
          return true;
        } else if (rol === 'ROLE_ADMIN') {
          console.log("🚫 No es usuario, redirigiendo a dashboard de admin...");
          return this.router.parseUrl('/dashboard-admin'); // 👈 redirección correcta
        }
      }

      console.log("🚫 No logueado, redirigiendo a /login...");
      return this.router.parseUrl('/login');
    } catch (error) {
      console.error('❌ Error en UserGuard:', error);
      return this.router.parseUrl('/login');
    }
  }
}
