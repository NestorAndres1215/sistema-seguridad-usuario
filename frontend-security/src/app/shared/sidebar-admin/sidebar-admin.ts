import { Component } from '@angular/core';
import { GoogleService } from '../../core/services/google.service';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar-admin',
 imports: [CommonModule, RouterModule],
  templateUrl: './sidebar-admin.html',
  styleUrl: './sidebar-admin.css'
})
export class SidebarAdmin {
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
