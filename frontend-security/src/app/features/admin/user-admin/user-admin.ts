import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { UserActive } from "../user-active/user-active";
import { UserInactive } from "../user-inactive/user-inactive";
import { UserBlocked } from "../user-blocked/user-blocked";
import { UserSuspend } from "../user-suspend/user-suspend";
import { Titulo } from "../../../shared/titulo/titulo";

@Component({
  selector: 'app-user-admin',
   standalone: true,
  imports: [CommonModule, MatTabsModule, UserActive, UserInactive, UserBlocked, UserSuspend, Titulo],
  templateUrl: './user-admin.html',
  styleUrl: './user-admin.css'
})
export class UserAdmin {
  constructor() { }

  activeTab: number = 0;
}
