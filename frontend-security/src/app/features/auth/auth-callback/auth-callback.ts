import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GoogleService } from '../../../core/services/google.service';
import { AlertService } from '../../../core/services/alert.service';

@Component({
  selector: 'app-auth-callback',
  imports: [],
  templateUrl: './auth-callback.html',
  styleUrl: './auth-callback.css'
})
export class AuthCallback {
  constructor(
    private route: ActivatedRoute,
    private alertService: AlertService,
    private authService: GoogleService,
    private router: Router
  ) { }


  ngOnInit() {
    this.callBack();

  }

  callBack() {
    const code = this.route.snapshot.queryParamMap.get('code');

    if (!code) {
      this.alertService.error('Error de autenticación', 'No se recibió el código de Google.');

      return;
    }

    this.authService.loginWithCode(code).subscribe({
      next: (res) => {

        localStorage.setItem('jwt', res.token);
        this.authService.getCurrentUser().subscribe({
          next: (user) => {
            console.log('👤 Usuario actual:', user.username);
            localStorage.setItem('username', user.email)
            this.router.navigate(['/dashboard']);
          },
          error: () => {

            this.router.navigate(['/login']);
          },
        });
      },
      error: () => {
        this.alertService.error('Error', 'No se pudo completar la autenticación con Google');
        this.router.navigate(['/login']);
      },
    });

  }
}
