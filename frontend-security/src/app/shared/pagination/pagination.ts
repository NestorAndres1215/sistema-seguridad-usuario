import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pagination.html',
  styleUrls: ['./pagination.css']
})
export class PaginationComponent {
  @Input() currentPage = 1;
  @Input() totalPages = 1;
  @Output() pageChanged = new EventEmitter<number>();

  previousPage() {
    if (this.currentPage > 1) this.pageChanged.emit(--this.currentPage);
  }

  nextPage() {
    if (this.currentPage < this.totalPages) this.pageChanged.emit(++this.currentPage);
  }
}
