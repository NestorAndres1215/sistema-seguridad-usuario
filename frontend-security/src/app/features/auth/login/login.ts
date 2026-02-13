import { Component } from '@angular/core';
import { GoogleService } from '../../../core/services/google.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from '../../../core/services/alert.service';
import { Login_IS } from '../../../models/loginIS';
import { ROLES } from '../../../core/constants/role.contants';


@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  formulario!: FormGroup;
  constructor(
    private fb: FormBuilder,
    private router: Router, private authService: GoogleService,
    private googleService: GoogleService,
    private alertService: AlertService
  ) { }


  login() {
    this.googleService.login();
  }
  
  operar() {
    if (this.formulario.valid) {
      const login: Login_IS = {
        login: this.formulario.get('login')?.value,
        password: this.formulario.get('password')?.value
      };

      this.authService.generateToken(login).subscribe({
        next: (data: any) => {

          // Guardar token
          this.authService.setToken(data.token);

          // Obtener usuario actual
          this.authService.getCurrentUser().subscribe({
            next: (user) => {

              console.log('Usuario actual:', user.role.name);
              const rol = user.role.name
              console.log(ROLES.ROLE_USER); // "ROLE_USER"

              if (rol == ROLES.ROLE_ADMIN) {
                localStorage.setItem('username', user.username)
                console.log("INGRESO A ADMINISTRADOR" + user)
                this.router.navigate(['/dashboard-admin']);
              } else {
                console.log("INGRESO USER")
                localStorage.setItem('username', user.username)
                this.router.navigate(['/dashboard']);
              }
            },
            error: (error) => {
              console.error('Error obteniendo usuario actual:', error);
            },
            complete: () => {
              console.log('Solicitud de usuario completada');
            }
          });
        },
        error: (error) => {

          console.error('Error generando token:', error);
          this.alertService.error('Error', 'Credenciales incorrectas o servidor no disponible.');
        },
        complete: () => {
          console.log('Proceso de autenticación completado');
        }
      });

    } else {
      this.alertService.warning(
        'Campos incompletos',
        'Por favor, completa todos los campos requeridos.'
      );
      this.formulario.markAllAsTouched();
    }
  }

  ngOnInit(): void {

    this.initForm();
  }


  initForm() {
    this.formulario = this.fb.group({
      login: ['', Validators.required],
      password: ['', Validators.required]
    });
  }
  register() {
    this.router.navigate(['/register'])
  }
}
