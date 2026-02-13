import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  success(title: string, text?: string) {
    Swal.fire({
      icon: 'success',
      title,
      text,
      confirmButtonColor: '#3085d6'
    });
  }

  error(title: string, text?: string) {
    Swal.fire({
      icon: 'error',
      title,
      text,
      confirmButtonColor: '#d33'
    });
  }

  warning(title: string, text?: string) {
    Swal.fire({
      icon: 'warning',
      title,
      text,
      confirmButtonColor: '#f0ad4e'
    });
  }

  info(title: string, text?: string) {
    Swal.fire({
      icon: 'info',
      title,
      text,
      confirmButtonColor: '#17a2b8'
    });
  }

  confirm(title: string, text: string): Promise<boolean> {
    return Swal.fire({
      title,
      text,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí',
      cancelButtonText: 'No',
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33'
    }).then(result => result.isConfirmed);
  }
}
