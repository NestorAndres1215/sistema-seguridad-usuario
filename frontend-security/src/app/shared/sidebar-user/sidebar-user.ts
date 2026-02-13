import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { GoogleService } from '../../core/services/google.service';

@Component({
  selector: 'app-sidebar-user',
  imports: [CommonModule, RouterModule],

  templateUrl: './sidebar-user.html',
  styleUrl: './sidebar-user.css'
})
export class SidebarUser {
  user: any;
  constructor(
    private authService: GoogleService,
    private router: Router
  ) {

  }
  username: any
  ngOnInit() {
    this.username = localStorage.getItem('username')
  }
  isActive(path: string): boolean {
    return this.router.url === path;
  }
  isLoggedIn = false;
  contenido: any;
  hayContenidoEnPagina(): boolean {
    // Verificar si la variable "contenido" tiene algún valor
    return !!this.contenido;
  }
  status = false;
  addToggle() {
    this.status = !this.status;
  }
  logout() {
    this.authService.logout().subscribe({
      next: (res) => {

        this.router.navigate(['/login']);

        console.log('🔴 Sesión cerrada:', res);
      },
      error: (err) => {
        console.error('❌ Error en logout:', err);
      }
    });
  }
}
