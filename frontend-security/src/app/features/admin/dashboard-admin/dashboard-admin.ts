import { Component, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { UserService } from '../../../core/services/user.service';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.html',
  styleUrls: ['./dashboard-admin.css']
})
export class DashboardAdmin implements OnInit {

  constructor(private userService: UserService) { }

  activo = 0;
  inactivo = 0;
  suspendido = 0;
  bloqueado = 0;
  ngOnInit(): void {
    this.allPorcentaje(); // ✅ Cargamos gráfico de estados dinámico
    this.loadRoleChart(); // ✅ Gráfico de roles estático (por ahora)
  }

  // ✅ Llamamos al backend y generamos el gráfico dinámico
  allPorcentaje() {
    this.userService.getUserStatusPercentages().subscribe({
      next: (data) => {
        console.log('Porcentajes:', data);

        // ✅ Mapeamos correctamente los nombres del backend
        const labels = data.map((item: any) => this.formatStatusLabel(item.statusCode));
        const valores = data.map((item: any) => item.porcentaje);
        const cantidad = data.map((item: any) => item.totalUsuarios);
        console.log("HOLA" + cantidad)
        data.forEach((item) => {
          switch (item.statusCode) {
            case 'ACTIVE':
              this.activo = item.totalUsuarios;
              break;
            case 'INACTIVE':
              this.inactivo = item.totalUsuarios;

              break;
            case 'SUSPEND':
              this.suspendido = item.totalUsuarios;

              break;
            case 'BLOCKED':
              this.bloqueado = item.totalUsuarios;

              break;
          }
        });

        this.renderStatusChart(labels, valores);
      },
      error: (err) => {
        console.error('Error al obtener porcentajes', err);
      }
    });
  }

  // ✅ Renderizamos gráfico tipo doughnut
  renderStatusChart(labels: string[], valores: number[]) {
    new Chart('statusChart', {
      type: 'doughnut',
      data: {
        labels,
        datasets: [{
          data: valores,
          backgroundColor: ['#198754', '#6c757d', '#ffc107', '#dc3545']
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: 'bottom' },
          tooltip: {
            callbacks: {
              label: function (context: any) {
                return `${context.label}: ${context.parsed}%`;
              }
            }
          }
        }
      }
    });
  }

  // ✅ Traducimos o formateamos los códigos a etiquetas más amigables
  formatStatusLabel(code: string): string {
    switch (code) {
      case 'ACTIVE': return 'Activo';
      case 'INACTIVE': return 'Inactivo';
      case 'SUSPEND': return 'Suspendido';
      case 'BLOCKED': return 'Bloqueado';
      default: return code;
    }
  }


  // ✅ Gráfico de roles (estático de momento)
  loadRoleChart() {
    new Chart('roleChart', {
      type: 'bar',
      data: {
        labels: ['Admin', 'Editor', 'Usuario', 'Invitado'],
        datasets: [{
          label: 'Cantidad',
          data: [5, 10, 30, 120, 45],
          backgroundColor: ['#0d6efd', '#20c997', '#6f42c1', '#ffc107']
        }]
      },
      options: {
        responsive: true,
        plugins: { legend: { display: false } },
        scales: { y: { beginAtZero: true } }
      }
    });
  }
}
