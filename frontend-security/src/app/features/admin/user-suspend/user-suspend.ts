import { Component } from '@angular/core';
import { ModalEliminacion } from '../../../shared/modal-eliminacion/modal-eliminacion';
import { Respuesta } from '../../../models/respuesta';
import { UserService } from '../../../core/services/user.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { AuthService } from '../../../core/services/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-suspend',
  imports: [FormsModule, MatDialogModule],
  templateUrl: './user-suspend.html',
  styleUrl: './user-suspend.css'
})
export class UserSuspend {
  users: any[] | null = null;
  currentPage = 1;
  itemsPerPage = 10; 
  constructor(private userService: UserService, private dialog: MatDialog, private authService: AuthService) { }

  ngOnInit(): void {

    const savedItems = localStorage.getItem('itemsPerPage');
    if (savedItems) {
      this.itemsPerPage = parseInt(savedItems, 10);
    }

    this.loadUsers();
  }

  descativar(row: any) {
    console.log(row)
    const dialogEliminar = this.dialog.open(ModalEliminacion, {
      disableClose: true,
      width: '500px',
      data: {
        row,
        titulo: 'Restaurar',
        subtitulo: `¿Deseas restaurar el usuario ${row.username} con el codigo ${row.id} ? `
      },

    });

    dialogEliminar.afterClosed().subscribe((respuesta: Respuesta) => {
      if (respuesta?.boton != 'CONFIRMAR') return;
      this.userService.activarUsuario(row.id).subscribe(result => {
        this.loadUsers();
      });
    })
  }

  loadUsers(): void {
    this.userService.getUsersSuspend().subscribe({
      next: (data) => (this.users = data),
      error: (err) => {
        this.users = [];
      },
    });
  }


  get totalPages(): number {
    return this.users ? Math.ceil(this.users.length / this.itemsPerPage) : 0;
  }

  paginatedUsers(): any[] {
    if (!this.users) return [];
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.users.slice(startIndex, startIndex + this.itemsPerPage);
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
