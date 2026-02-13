import { Component } from '@angular/core';
import { GoogleService } from '../../../core/services/google.service';
import { Router } from '@angular/router';
import { SidebarUser } from "../../../shared/sidebar-user/sidebar-user";

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard {
  constructor(
    private router: Router,
    private authService: GoogleService,

  ) { }

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
    });
  }

}
