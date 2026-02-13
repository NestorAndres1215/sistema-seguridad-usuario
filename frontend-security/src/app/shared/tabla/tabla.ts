import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Button } from "../button/button";

@Component({
  selector: 'app-tabla',
  imports: [CommonModule, Button],
  templateUrl: './tabla.html',
  styleUrl: './tabla.css'
})
export class Tabla {

  @Input() icono: string = '';
  @Input() titulo: string = '';
  @Input() columnas: { etiqueta: string; clave: string }[] = [];

  @Input() onRegistrar!: () => void;
  @Input() botonesConfig: any = {};
  @Input() datos: any[] = [];

  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  datos_vacio: string = 'No hay datos para mostrar.';

  columnaOrden: string = '';
  direccionOrden: 'asc' | 'desc' = 'asc';

  @Input() onVer!: (fila: any) => void;
  @Input() onActualizar!: (fila: any) => void;
  @Input() onDesactivar!: (fila: any) => void;
  @Input() onActivar!: (fila: any) => void;
  @Input() onSuspender!: (fila: any) => void;
  @Input() onListado!: (fila: any) => void;
  @Input() onInhabilitar!: (fila: any) => void;
  @Input() onBloquear!: (fila: any) => void;
  @Input() onImprimir!: (fila: any) => void;
  @Input() onCancelar!: (fila: any) => void;

  hasActionButtons(): boolean {
    return (
      this.botonesConfig.actualizar ||
      this.botonesConfig.activar ||
      this.botonesConfig.suspender ||
      this.botonesConfig.desactivar ||
      this.botonesConfig.inhabilitar ||
      this.botonesConfig.bloquear ||
      this.botonesConfig.imprimir ||
      this.botonesConfig.listado ||
      this.botonesConfig.ver ||
      this.botonesConfig.cancelar
    );
  }

  obtenerValor(obj: any, clave: string): any {
    return clave.split('.').reduce((valor, parte) => valor?.[parte], obj);
  }

  ordenarPor(campo: string) {
    if (this.columnaOrden === campo) {
      this.direccionOrden = this.direccionOrden === 'asc' ? 'desc' : 'asc';
    } else {
      this.columnaOrden = campo;
      this.direccionOrden = 'asc';
    }

    this.datos.sort((a, b) => {
      const valorA = this.obtenerValor(a, campo);
      const valorB = this.obtenerValor(b, campo);

      if (valorA == null) return 1;
      if (valorB == null) return -1;

      if (typeof valorA === 'number' && typeof valorB === 'number') {
        return this.direccionOrden === 'asc'
          ? valorA - valorB
          : valorB - valorA;
      }

      return this.direccionOrden === 'asc'
        ? valorA.toString().localeCompare(valorB.toString())
        : valorB.toString().localeCompare(valorA.toString());
    });
  }
}
