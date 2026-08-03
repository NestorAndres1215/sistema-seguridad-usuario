import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { GoogleService } from '../../core/services/google.service';

@Component({
  selector: 'app-sidebar-user',
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar-user.html',
  styleUrl: './sidebar-user.css',
})
export class SidebarUser implements OnInit {
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
      console.error('Error obteniendo usuario actual:', error);

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
      console.error('❌ Error cerrando sesión:', error);
    }
  }
}
