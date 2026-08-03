import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { firstValueFrom } from 'rxjs';

import { AlertService } from '../../../core/services/alert.service';
import { Registrar } from '../../../models/registrar';
import { GoogleService } from '../../../core/services/google.service';
import { Router } from '@angular/router';
import { UserService } from '../../../core/services/user.service';
import { MENSAJES } from '../../../core/constants/messages';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css'],
})
export class Register implements OnInit {
  formulario!: FormGroup;

  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private router: Router,
    private authService: GoogleService,
    private alertService: AlertService,
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.formulario = this.fb.group({
      name: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  async operar(): Promise<void> {
    if (!this.formulario.valid) {
      this.alertService.warning(MENSAJES.WARNING, MENSAJES.FILL_FIELDS);
      this.formulario.markAllAsTouched();
      return;
    }

    const usuario: Registrar = {
      name: this.formulario.get('name')?.value.trim(),
      username: this.formulario.get('username')?.value.trim(),
      email: this.formulario.get('email')?.value.trim(),
      password: this.formulario.get('password')?.value,
    };

    try {
      await firstValueFrom(this.userService.createUser(usuario));
      this.alertService.success(MENSAJES.SUCCESS, MENSAJES.WELCOME);
      this.formulario.reset();
      await this.router.navigate(['/login']);
    } catch (err: any) {
      const message = err?.error?.message ?? '';
      this.alertService.error('Error', message);
    }
  }

  registrarConGoogle(): void {
    this.authService.login();
  }

  volverLogin(): void {
    this.router.navigate(['/login']);
  }
}
