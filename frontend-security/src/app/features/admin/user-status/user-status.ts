import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { StatusService } from '../../../core/services/status.service';
import { PaginationComponent } from '../../../shared/pagination/pagination';
import { Tabla } from '../../../shared/tabla/tabla';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-user-status',
  imports: [FormsModule, PaginationComponent, Tabla],
  templateUrl: './user-status.html',
  styleUrl: './user-status.css',
})
export class UserStatus {
  status: any[] = [];
  currentPage = 1;
  itemsPerPage = 10;

  columnas = [
    { clave: 'id', etiqueta: 'Id' },
    { clave: 'code', etiqueta: 'Nombre' },
  ];

  constructor(private statusService: StatusService) {}

  ngOnInit(): void {
    const savedItems = localStorage.getItem('itemsPerPage');
    if (savedItems) {
      this.itemsPerPage = parseInt(savedItems, 10);
    }

    this.loadUsers();
  }

  async loadUsers(): Promise<void> {
    try {
      this.status = await firstValueFrom(this.statusService.getAllStatus());
    } catch (error) {
      console.error('Error al cargar estados:', error);

      this.status = [];
    }
  }

  get totalPages(): number {
    return this.status.length
      ? Math.ceil(this.status.length / this.itemsPerPage)
      : 1;
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
