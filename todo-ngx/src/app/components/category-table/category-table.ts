import { Component, inject, Input } from "@angular/core";
import { Category, categorySchema } from "../../../types";
import { MatTableModule } from "@angular/material/table";
import { MatMenuModule } from "@angular/material/menu";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { MatDialog } from "@angular/material/dialog";
import { UpdateCategoryDialog } from "../update-category-dialog/update-category-dialog";
import { HttpClient } from "@angular/common/http";

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
  private readonly http = inject(HttpClient)
  private readonly dialog = inject(MatDialog)

  @Input()
  categories: Category[] = []
  
  readonly displayedColumns: string[] = ['name', 'slug', 'color', 'operation']

  openUpdateCategoryDialog(currentCategory: Category) {
    const dialogRef = this.dialog.open(UpdateCategoryDialog, {
      data: currentCategory
    })

    dialogRef.afterClosed().subscribe(result => {
      const safeParsedCategory = categorySchema.safeParse(result)

      if (safeParsedCategory.success) {
        this.http.put<Category>(`/api/categories/${safeParsedCategory.data.id}`, {
          ...safeParsedCategory.data
        }).subscribe({
          next: (category) => this.categories = this.categories
            .with(
              this.categories.indexOf(currentCategory),
              category
            ),
          error: (err) => console.error(err)
        })
      }
    })
  }
}
