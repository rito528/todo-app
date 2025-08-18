import { Component, EventEmitter, inject, Input, Output } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { Category, Todo } from "../../../types";
import { HttpClient } from "@angular/common/http";
import { MatInputModule } from "@angular/material/input";
import { MatOptionModule } from "@angular/material/core";
import { MatSelectModule } from "@angular/material/select";
import { MatButtonModule } from "@angular/material/button";

@Component({
  selector: 'create-todo-form',
  templateUrl: 'create-todo-form.html',
  styleUrl: 'create-todo-form.scss',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatOptionModule,
    MatButtonModule,
  ]
})
export class CreateTodoForm {
  private http = inject(HttpClient)

  @Input()
  todos: Todo[] = []

  @Input()
  categories: Category[] = []

  @Output()
  createTodoEventEmitter: EventEmitter<Todo> = new EventEmitter()

  createTodoForm = new FormGroup({
    title: new FormControl('', [
      Validators.required,
      Validators.minLength(1),
      Validators.maxLength(255)
    ]),
    body: new FormControl('', [
      Validators.required,
      Validators.minLength(1)
    ]),
    category: new FormControl<undefined | number>(undefined)
  })

  onCreateTodo() {
    if (this.createTodoForm.valid) {
      this.http.post<Todo>('/api/todos', {
        title: this.createTodoForm.controls.title.value,
        body: this.createTodoForm.controls.body.value,
        categoryId: this.createTodoForm.controls.category.value
      }).subscribe({
        next: (todo) => {
          this.todos = [...this.todos, todo]
          this.createTodoEventEmitter.emit(todo)
          this.createTodoForm.reset({
            title: '',
            body: '',
            category: undefined
          })
        },
        error: (err) => console.error(err)
      })
    }
  }
}
