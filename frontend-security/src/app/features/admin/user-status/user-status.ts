import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { StatusService } from '../../../core/services/status.service';

@Component({
  selector: 'app-user-status',
  imports: [FormsModule],
  templateUrl: './user-status.html',
  styleUrl: './user-status.css'
})
export class UserStatus {
  status: any[] | null = null;
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
    return this.status ? Math.ceil(this.status.length / this.itemsPerPage) : 0;
  }

  paginatedUsers(): any[] {
    if (!this.status) return [];
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.status.slice(startIndex, startIndex + this.itemsPerPage);
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
