import { Component, inject, Injectable } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatTableModule } from '@angular/material/table';
import { Category, Todo } from '../types';
import { TodoStatePipe } from './pipes/todo-state-pipe';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatDialog } from '@angular/material/dialog';
import { todoUpdateFormSchema, UpdateTodoDialog } from './update-dialog/update-todo-dialog';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    MatTableModule,
    TodoStatePipe,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatIconModule,
    MatMenuModule,
  ],
  templateUrl: './app.html',
  styleUrl: './app.sass'
})
@Injectable({ providedIn: 'root' })
export class App {
  todos: Todo[] = []
  categories: Category[] = []

  private http = inject(HttpClient)
  readonly dialog = inject(MatDialog)

  constructor() {
    this.http.get<Todo[]>('/api/todos').subscribe({
      next: (todos) => this.todos = todos,
      error: (err) => console.error(err)
    })

    this.http.get<Category[]>('/api/categories').subscribe({
      next: (categories) => this.categories = categories,
      error: (err) => console.error(err)
    })
  }

  displayedColumns: string[] = ["title", "body", "state", "category", "operation"]

  createTodoForm = new FormGroup({
    title: new FormControl(''),
    body: new FormControl(''),
    category: new FormControl<undefined | number>(undefined)
  })

  onSubmitCreateTodo() {
    this.http.post<Todo>('/api/todos', {
      title: this.createTodoForm.controls.title.value,
      body: this.createTodoForm.controls.body.value,
      categoryId: this.createTodoForm.controls.category.value
    }).subscribe({
      next: (todo) => this.todos = [...this.todos, todo],
      error: (err) => console.error(err)
    })
  }

  openUpdateTodoDialog(currentTodo: Todo) {
    const dialogRef = this.dialog.open(UpdateTodoDialog, {
      data: {
        todo: {
          title: currentTodo.title,
          body: currentTodo.body,
          state: currentTodo.state,
          categoryId: currentTodo.category?.id
        },
        categories: this.categories
      }
    })

    dialogRef.afterClosed().subscribe(result => {
      const safeParsedForm = todoUpdateFormSchema.safeParse(result)

      if (safeParsedForm.success) {
        this.http.put<Todo>(`/api/todos/${currentTodo.id}`, {
          ...safeParsedForm.data.todo
        }).subscribe({
          next: (todo) => this.todos = this.todos
            .with(
              this.todos.indexOf(currentTodo),
              todo
            ),
          error: (err) => console.error(err)
        })
      }
    })
  }

  onDeleteTodo(todoId: number) {
    this.http.delete(`/api/todos/${todoId}`).subscribe({
      next: () => this.todos = this.todos.filter((todo) => todo.id !== todoId),
      error: (err) => console.error(err)
    })
  }
}
