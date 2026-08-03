import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { GoogleService } from '../../../core/services/google.service';
import { AlertService } from '../../../core/services/alert.service';

@Component({
  selector: 'app-auth-callback',
  imports: [],
  templateUrl: './auth-callback.html',
  styleUrl: './auth-callback.css',
})
export class AuthCallback {
  constructor(
    private route: ActivatedRoute,
    private alertService: AlertService,
    private authService: GoogleService,
    private router: Router,
  ) {}

  async ngOnInit(): Promise<void> {
    await this.callBack();
  }

  async callBack(): Promise<void> {
    const code = this.route.snapshot.queryParamMap.get('code');

    if (!code) {
      this.alertService.error(
        'Error de autenticación',
        'No se recibió el código de Google.',
      );

      return;
    }

    try {
      // Backend crea la cookie JWT automáticamente
      await firstValueFrom(this.authService.loginWithCode(code));

      // Validamos usuario usando la cookie
      const user = await firstValueFrom(this.authService.getCurrentUser());

      console.log('👤 Usuario actual:', user.username);

      await this.router.navigate(['/dashboard']);
    } catch (error) {
      console.error('Error Google Login:', error);

      this.alertService.error(
        'Error',
        'No se pudo completar la autenticación con Google',
      );

      await this.router.navigate(['/login']);
    }
  }
}
