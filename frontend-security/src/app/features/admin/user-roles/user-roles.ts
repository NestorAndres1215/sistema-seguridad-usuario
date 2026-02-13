import { Component } from '@angular/core';
import { RoleService } from '../../../core/services/role.service';
import { FormsModule } from '@angular/forms';
import { PaginationComponent } from "../../../shared/pagination/pagination";

@Component({
  selector: 'app-user-roles',
  imports: [FormsModule, PaginationComponent],
  templateUrl: './user-roles.html',
  styleUrl: './user-roles.css'
})
export class UserRoles {
   role: any[] = [];
  currentPage = 1;
  itemsPerPage = 10;

  constructor(private roleService: RoleService) {}

  ngOnInit(): void {
    const savedItems = localStorage.getItem('itemsPerPage');
    if (savedItems) this.itemsPerPage = parseInt(savedItems, 10);

    this.loadUsers();
  }

  loadUsers(): void {
    this.roleService.getAllRole().subscribe({
      next: (data) => (this.role = data),
      error: () => (this.role = []),
    });
  }

  get totalPages(): number {
    return this.role.length ? Math.ceil(this.role.length / this.itemsPerPage) : 1;
  }

  paginatedUsers(): any[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.role.slice(start, start + this.itemsPerPage);
  }

  onItemsPerPageChange(): void {
    localStorage.setItem('itemsPerPage', this.itemsPerPage.toString());
    this.currentPage = 1;
  }

  onPageChanged(newPage: number) {
    this.currentPage = newPage;
  }
}
