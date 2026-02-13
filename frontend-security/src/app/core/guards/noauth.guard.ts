import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { firstValueFrom } from 'rxjs';
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

        const destino: string =
          role === 'ROLE_ADMIN' ? '/dashboard-admin' :
            role === 'ROLE_USER' ? '/dashboard' :
              '/login';

        return this.router.parseUrl(destino);

      } catch (error) {
        return this.router.parseUrl('/login');
      }
    }

    return true;
  }
}