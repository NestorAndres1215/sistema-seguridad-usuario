import { Component } from '@angular/core';
import { GoogleService } from '../../../core/services/google.service';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from '../../../core/services/alert.service';
import { Login_IS } from '../../../models/loginIS';
import { ROLES } from '../../../core/constants/role.contants';
import { MENSAJES } from '../../../core/constants/messages';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  formulario!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: GoogleService,
    private alertService: AlertService,
  ) {}

  login(): void {
    this.authService.login();
  }

  async operar(): Promise<void> {
    if (!this.formulario.valid) {
      this.alertService.warning(MENSAJES.WARNING, MENSAJES.FILL_FIELDS);
      this.formulario.markAllAsTouched();
      return;
    }

    const login: Login_IS = {
      login: this.formulario.get('login')?.value,
      password: this.formulario.get('password')?.value,
    };

    try {
      await firstValueFrom(this.authService.generateToken(login));
      const user = await firstValueFrom(this.authService.getCurrentUser());
      const rol = user?.role?.name;

      if (rol === ROLES.ROLE_ADMIN) {
        await this.router.navigate(['/dashboard-admin']);
      } else if (rol === ROLES.ROLE_USER) {
        await this.router.navigate(['/dashboard']);
      } else {
        this.alertService.error('Error', 'Rol no válido');
      }
    } catch (error) {
      this.alertService.error('Error', MENSAJES.LOGIN_ERROR);
    }
  }

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.formulario = this.fb.group({
      login: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  register(): void {
    this.router.navigate(['/register']);
  }
}
