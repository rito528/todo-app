import { Component, Input } from "@angular/core";
import { Category } from "../../../types";
import { MatTableModule } from "@angular/material/table";
import { MatMenuModule } from "@angular/material/menu";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";

@Component({
  selector: 'category-table',
  templateUrl: 'category-table.html',
  imports: [
    MatTableModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule,
  ]
})
export class CategoryTable {
  @Input()
  categories: Category[] = []

  readonly displayedColumns: string[] = ['name', 'slug', 'color', 'operation']
}
