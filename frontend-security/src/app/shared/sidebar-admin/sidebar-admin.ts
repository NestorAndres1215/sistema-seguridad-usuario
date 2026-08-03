import { Component, OnInit } from '@angular/core';
import { GoogleService } from '../../core/services/google.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-sidebar-admin',
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar-admin.html',
  styleUrl: './sidebar-admin.css',
})
export class SidebarAdmin implements OnInit {
  user: any = null;
  username = '';

  isLoggedIn = false;
  contenido: any;
  status = false;

  constructor(
    private authService: GoogleService,
    private router: Router,
  ) {}

  async ngOnInit(): Promise<void> {
    await this.loadUser();
  }

  async loadUser(): Promise<void> {
    try {
      const user = await firstValueFrom(this.authService.getCurrentUser());

      this.user = user;

      this.username = user.username;

      this.isLoggedIn = true;
    } catch (error) {
      console.error('Error obteniendo usuario:', error);

      this.user = null;

      this.username = '';

      this.isLoggedIn = false;

      await this.router.navigate(['/login']);
    }
  }

  isActive(path: string): boolean {
    return this.router.url === path;
  }

  hayContenidoEnPagina(): boolean {
    return !!this.contenido;
  }

  addToggle(): void {
    this.status = !this.status;
  }

  async logout(): Promise<void> {
    try {
      await firstValueFrom(this.authService.logout());

      this.user = null;

      this.username = '';

      this.isLoggedIn = false;

      await this.router.navigate(['/login']);
    } catch (error) {
      console.error('❌ Error en logout:', error);
    }
  }
}
