import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-principal',
  imports: [],
  templateUrl: './principal.html',
  styleUrl: './principal.css'
})
export class Principal {

 constructor(private router: Router) { }
 
  register() {
    this.router.navigate(['/register'])
  }

  iniciarSesion() {
    this.router.navigate(['/login']);
  }
}
