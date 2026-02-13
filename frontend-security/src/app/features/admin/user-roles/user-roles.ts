import { Component } from '@angular/core';
import { RoleService } from '../../../core/services/role.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-roles',
  imports: [FormsModule],
  templateUrl: './user-roles.html',
  styleUrl: './user-roles.css'
})
export class UserRoles {
  role: any[] | null = null;
  currentPage = 1;
  itemsPerPage = 10;

  constructor(private roleService: RoleService) { }

  ngOnInit(): void {
    const savedItems = localStorage.getItem('itemsPerPage');
    if (savedItems) {
      this.itemsPerPage = parseInt(savedItems, 10);
    }

    this.loadUsers();
  }
  loadUsers(): void {
    this.roleService.getAllRole().subscribe({
      next: (data) => (this.role = data),
      error: (err) => {

        this.role = [];
      },
    });
  }

  get totalPages(): number {
    return this.role ? Math.ceil(this.role.length / this.itemsPerPage) : 0;
  }


  paginatedUsers(): any[] {
    if (!this.role) return [];
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.role.slice(startIndex, startIndex + this.itemsPerPage);
  }

  onItemsPerPageChange(): void {
    localStorage.setItem('itemsPerPage', this.itemsPerPage.toString());
    this.currentPage = 1;
  }


  nextPage(): void {
    if (this.currentPage < this.totalPages) this.currentPage++;
  }

  previousPage(): void {
    if (this.currentPage > 1) this.currentPage--;
  }
}
