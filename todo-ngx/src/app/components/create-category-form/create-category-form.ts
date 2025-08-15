import { Component, EventEmitter, inject, Output } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { HttpClient } from "@angular/common/http";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { Category } from "../../../types";

@Component({
  selector: 'create-category-form',
  templateUrl: 'create-category-form.html',
  styleUrl: 'create-category-form.scss',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ]
})
export class CreateCategoryForm {
  private http = inject(HttpClient)

  @Output()
  createCategoryEventEmitter: EventEmitter<Category> = new EventEmitter()

  createCategoryForm = new FormGroup({
    name: new FormControl('', [
      Validators.minLength(1),
      Validators.maxLength(32)
    ]),
    slug: new FormControl('', [
      Validators.pattern('^[0-9A-Za-z]+$')
    ]),
    color: new FormControl('#000000'),
  })

  onCreateCategory() {
    if (this.createCategoryForm.valid) {
      this.http.post<Category>('/api/categories', {
        name: this.createCategoryForm.controls.name.value,
        slug: this.createCategoryForm.controls.slug.value,
        color: this.createCategoryForm.controls.color.value,
      }).subscribe({
        next: (category) => {
          this.createCategoryEventEmitter.emit(category)
          this.createCategoryForm.reset({
            name: '',
            slug: '',
            color: '#000000'
          })
        },
        error: (err) => console.error(err)
      })
    }
  }
}
