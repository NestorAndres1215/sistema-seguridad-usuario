import { Component } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import { GoogleService } from '../../../core/services/google.service';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  constructor(
    private router: Router,
    private authService: GoogleService,
  ) {}

  async logout(): Promise<void> {
    try {
      await firstValueFrom(this.authService.logout());
      await this.router.navigate(['/login']);
    } catch (error) {
      console.error('Error al cerrar sesión:', error);
    }
  }
}
