import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user.html',
  styleUrls: ['./user.css'],
})
export class User implements OnInit {
  isLoggedIn = false;
  user: any = null;

  constructor(private authService: AuthService) {}

  async ngOnInit(): Promise<void> {
    await this.listUser();
  }

  async listUser(): Promise<void> {
    try {
      this.user = await firstValueFrom(this.authService.getCurrentUser());
      this.isLoggedIn = true;
    } catch (error) {
      this.user = null;
      this.isLoggedIn = false;
    }
  }
}
