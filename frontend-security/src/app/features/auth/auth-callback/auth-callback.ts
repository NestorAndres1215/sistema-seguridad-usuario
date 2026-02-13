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

    // 2️⃣ Llamar al backend para intercambiar code por token
    this.authService.loginWithCode(code).subscribe({
      next: (res) => {
        console.log('✅ Login exitoso');
        console.log('Token:', res.token);
        console.log('Email:', res.email);
        console.log('Name:', res.name);

        // Guardar token en localStorage
        localStorage.setItem('jwt', res.token);
        this.authService.getCurrentUser().subscribe({
          next: (user) => {
            console.log('👤 Usuario actual:', user.username);
            localStorage.setItem('username', user.email)


            // Redirigir al dashboard
            this.router.navigate(['/dashboard']);
          },
          error: (err) => {
            console.error('❌ Error al obtener usuario:', err);
            this.router.navigate(['/login']);
          },
        });
      },
      error: (err) => {
        this.alertService.error('Error', 'No se pudo completar la autenticación con Google');
        this.router.navigate(['/login']);
      },
    });

  }
}
