import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
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
  styleUrls: ['./register.css']
})
export class Register implements OnInit {
  volverLogin() {
    this.router.navigate(['/login'])
  }

  formulario!: FormGroup;

  constructor(
    private userService: UserService,
    private fb: FormBuilder, private router: Router,
    private authService: GoogleService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.formulario = this.fb.group({
      name: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  operar() {
    if (this.formulario.valid) {
      const usuario: Registrar = {
        name: this.formulario.get('name')?.value.trim(),
        username: this.formulario.get('username')?.value.trim(),
        email: this.formulario.get('email')?.value.trim(),
        password: this.formulario.get('password')?.value
      };


      this.userService.createUser(usuario).subscribe({
        next: () => {

          this.alertService.success(MENSAJES.SUCCESS, MENSAJES.WELCOME);
          this.formulario.reset();
          this.router.navigate(['/login']);
        },
        error: (err) => {
          if (err.error?.message?.includes('Username already exists')) {
            this.alertService.error('Error', MENSAJES.USERNAME_EXISTS);
          } else if (err.error?.message?.includes('Email already exists')) {
            this.alertService.error('Error', MENSAJES.EMAIL_EXISTS);
          } else {
            this.alertService.error('Error', MENSAJES.GENERIC_ERROR);
          }

        }
      });

    } else {
      this.alertService.warning(MENSAJES.WARNING, MENSAJES.FILL_FIELDS);
      this.formulario.markAllAsTouched();
    }
  }


  registrarConGoogle() {
    this.authService.login();
  }
}
