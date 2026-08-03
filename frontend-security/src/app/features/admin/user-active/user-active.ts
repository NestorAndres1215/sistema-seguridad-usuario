import { Component } from '@angular/core';
import { UserService } from '../../../core/services/user.service';
import { FormsModule } from '@angular/forms';
import { ModalEliminacion } from '../../../shared/modal-eliminacion/modal-eliminacion';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { Respuesta } from '../../../models/respuesta';
import { AuthService } from '../../../core/services/auth.service';
import { PaginationComponent } from '../../../shared/pagination/pagination';
import { Tabla } from '../../../shared/tabla/tabla';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-user-active',
  imports: [FormsModule, MatDialogModule, PaginationComponent, Tabla],
  templateUrl: './user-active.html',
  styleUrl: './user-active.css',
})
export class UserActive {
  users: any[] = [];
  currentPage = 1;
  itemsPerPage = 10;
  columnas = [
    { clave: 'id', etiqueta: 'Codigo' },
    { clave: 'email', etiqueta: 'Correo' },
    { clave: 'name', etiqueta: 'Nombre' },
  ];

  botonesConfig = {
    bloquear: true,
    suspender: true,
    desactivar: true,
  };

  constructor(
    private userService: UserService,
    private dialog: MatDialog,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    const savedItems = localStorage.getItem('itemsPerPage');
    if (savedItems) {
      this.itemsPerPage = parseInt(savedItems, 10);
    }
    this.loadUsers();
  }

  async descativar(row: any): Promise<void> {
    const dialogEliminar = this.dialog.open(ModalEliminacion, {
      disableClose: true,
      width: '500px',
      data: {
        row,
        titulo: 'Restaurar',
        subtitulo: `¿Deseas restaurar el usuario ${row.username} con el código ${row.id}?`,
      },
    });

    const respuesta: Respuesta = await firstValueFrom(
      dialogEliminar.afterClosed(),
    );

    if (respuesta?.boton !== 'CONFIRMAR') {
      return;
    }

    try {
      await firstValueFrom(this.userService.inactivarUsuario(row.id));

      this.loadUsers();
    } catch (error) {
      console.error('Error al inactivar usuario:', error);
    }
  }

  async suspender(row: any): Promise<void> {
    const dialogEliminar = this.dialog.open(ModalEliminacion, {
      disableClose: true,
      width: '500px',
      data: {
        row,
        titulo: 'Suspender',
        subtitulo: `¿Deseas suspender el usuario ${row.username} con el código ${row.id}?`,
      },
    });

    const respuesta: Respuesta = await firstValueFrom(
      dialogEliminar.afterClosed(),
    );

    if (respuesta?.boton !== 'CONFIRMAR') {
      return;
    }

    try {
      await firstValueFrom(this.userService.suspenderUsuario(row.id));
      this.loadUsers();
    } catch (error) {
      console.error('Error al suspender usuario:', error);
    }
  }

  async bloquear(row: any): Promise<void> {
    const dialogEliminar = this.dialog.open(ModalEliminacion, {
      disableClose: true,
      width: '500px',
      data: {
        row,
        titulo: 'Bloquear',
        subtitulo: `¿Deseas bloquear el usuario ${row.username} con el código ${row.id}?`,
      },
    });

    const respuesta: Respuesta = await firstValueFrom(
      dialogEliminar.afterClosed(),
    );

    if (respuesta?.boton !== 'CONFIRMAR') {
      return;
    }

    try {
      await firstValueFrom(this.userService.blockedUsuario(row.id));

      this.loadUsers();
    } catch (error) {
      console.error('Error al bloquear usuario:', error);
    }
  }

  async loadUsers(): Promise<void> {
    try {
      this.users = await firstValueFrom(this.userService.getUsersActive());
    } catch (error) {
      console.error('Error al cargar usuarios:', error);

      this.users = [];
    }
  }
  
  get totalPages(): number {
    return this.users.length
      ? Math.ceil(this.users.length / this.itemsPerPage)
      : 1;
  }

  paginatedUsers(): any[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.users.slice(start, start + this.itemsPerPage);
  }

  onItemsPerPageChange(): void {
    localStorage.setItem('itemsPerPage', this.itemsPerPage.toString());
    this.currentPage = 1;
  }

  onPageChanged(newPage: number) {
    this.currentPage = newPage;
  }
}
