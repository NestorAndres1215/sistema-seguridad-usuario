import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user.html',
  styleUrls: ['./user.css']
})
export class User implements OnInit {
  isLoggedIn = false;
  user: any = null;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.listUser();
  }

  listUser() {
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.user = user;
        this.isLoggedIn = true;
      },
    });
  }
}
