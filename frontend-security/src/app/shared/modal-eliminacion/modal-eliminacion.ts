import { Component, Inject } from '@angular/core';
import { Respuesta } from '../../models/respuesta';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal-eliminacion',
  imports: [CommonModule, MatFormFieldModule, MatSelectModule],
  templateUrl: './modal-eliminacion.html',
  styleUrl: './modal-eliminacion.css'
})
export class ModalEliminacion {
  titulo: string = "";
  subtitulo: string = "";
  razon: string = "";
  meses: any[] = [];
  mesActual: string = "";
  mesCambio: string = "";

  constructor(private dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) private data: any,
 
    private sanitizer: DomSanitizer) { }


  ngOnInit(): void {
    console.log("🙌")
    this.titulo = this.data['titulo'];
    this.subtitulo = this.data['subtitulo'];
    this.razon = this.data['razon'];
  

  }

  close(mensaje: string): void {
    const respuesta: Respuesta = {
      boton: mensaje,
      razon: this.razon,
    }
  
    this.dialogRef.close(respuesta);
  }
  getHTML(): SafeHtml {
    return  this.sanitizer.bypassSecurityTrustHtml(this.subtitulo + this.mesActual + this.mesCambio);
  }
}
