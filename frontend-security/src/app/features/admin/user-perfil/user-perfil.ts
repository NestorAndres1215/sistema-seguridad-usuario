import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-perfil',
 imports: [CommonModule], // ✅ Importa CommonModule aquí
  templateUrl: './user-perfil.html',
  styleUrl: './user-perfil.css'
})
export class UserPerfil {
 isLoggedIn = false;
  user: any = null;

  constructor(private authService: AuthService) {}

  ngOnInit() {
    console.log('hola');
    this.listUser();
  }

  listUser() {
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        console.log('Usuario actual:', user);
        this.user = user;
        this.isLoggedIn = true;
      },
      error: (err) => {
        console.error('Error al obtener el usuario actual:', err);
        this.isLoggedIn = false;
      }
    });
  }
}
