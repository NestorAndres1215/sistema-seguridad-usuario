import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { StatusService } from '../../../core/services/status.service';
import { PaginationComponent } from "../../../shared/pagination/pagination";

@Component({
  selector: 'app-user-status',
  imports: [FormsModule, PaginationComponent],
  templateUrl: './user-status.html',
  styleUrl: './user-status.css'
})
export class UserStatus {
  status: any[] = [];
  currentPage = 1;
  itemsPerPage = 10; 

  constructor(private statusService: StatusService) { }

  ngOnInit(): void {

    const savedItems = localStorage.getItem('itemsPerPage');
    if (savedItems) {
      this.itemsPerPage = parseInt(savedItems, 10);
    }

    this.loadUsers();
  }
  loadUsers(): void {
    this.statusService.getAllStatus().subscribe({
      next: (data) => (this.status = data),
      error: (err) => {
        console.error('Error cargando usuarios:', err);
        this.status = [];
      },
    });
  }

  get totalPages(): number {
    return this.status.length ? Math.ceil(this.status.length / this.itemsPerPage) : 1;
  }

  paginatedUsers(): any[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.status.slice(start, start + this.itemsPerPage);
  }

  onItemsPerPageChange(): void {
    localStorage.setItem('itemsPerPage', this.itemsPerPage.toString());
    this.currentPage = 1;
  }

  onPageChanged(newPage: number) {
    this.currentPage = newPage;
  }
}
