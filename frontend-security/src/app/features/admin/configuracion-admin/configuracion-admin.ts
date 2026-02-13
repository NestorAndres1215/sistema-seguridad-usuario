import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { UserRoles } from "../user-roles/user-roles";
import { UserStatus } from "../user-status/user-status";
import { Titulo } from "../../../shared/titulo/titulo";

@Component({
  selector: 'app-configuracion-admin',
   standalone: true,
  imports: [CommonModule, MatTabsModule, UserRoles, UserStatus, Titulo],
  templateUrl: './configuracion-admin.html',
  styleUrl: './configuracion-admin.css'
})
export class ConfiguracionAdmin {
  constructor() { }

  ngOnInit(): void {
    
  }
  activeTab: number = 0;
}
