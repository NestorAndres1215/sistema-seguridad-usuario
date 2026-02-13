import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-user',
  standalone: true, // 👈 Importante si estás usando Angular standalone (sin AppModule)
  imports: [CommonModule], // 👈 necesario para usar *ngIf, *ngFor, ngClass, etc.
  templateUrl: './user.html',
  styleUrls: ['./user.css'] // 👈 debe ser plural, no "styleUrl"
})
export class User implements OnInit {
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
