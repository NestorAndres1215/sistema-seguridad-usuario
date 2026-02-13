import { Component } from '@angular/core';
import { GoogleService } from '../../../core/services/google.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from '../../../core/services/alert.service';
import { Login_IS } from '../../../models/loginIS';
import { ROLES } from '../../../core/constants/role.contants';
import { MENSAJES } from '../../../core/constants/messages';


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

          this.authService.setToken(data.token);
          this.authService.getCurrentUser().subscribe({
            next: (user) => {

              const rol = user.role.name

              if (rol == ROLES.ROLE_ADMIN) {
                localStorage.setItem('username', user.username)

                this.router.navigate(['/dashboard-admin']);
              } else {

                localStorage.setItem('username', user.username)
                this.router.navigate(['/dashboard']);
              }
            },
          });
        },
        error: () => {
           this.alertService.error('Error', MENSAJES.LOGIN_ERROR)
        }

      });

    } else {
   
        this.alertService.warning(MENSAJES.WARNING, MENSAJES.FILL_FIELDS);
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
