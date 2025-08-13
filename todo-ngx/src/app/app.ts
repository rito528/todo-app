import { Component, inject, Injectable, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatTableModule } from '@angular/material/table';
import { Todo } from '../types';
import { TodoStatePipe } from './pipes/todo-state-pipe';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    MatTableModule,
    TodoStatePipe,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    ReactiveFormsModule
  ],
  templateUrl: './app.html',
  styleUrl: './app.sass'
})
@Injectable({ providedIn: 'root' })
export class App {
  todos: Todo[] = []

  private http = inject(HttpClient)

  constructor() {
    this.http.get<Todo[]>('/api/todos').subscribe({
      next: (todos) => this.todos = todos,
      error: (err) => console.error(err)
    })
  }

  displayedColumns: string[] = ["title", "body", "state", "category"]

  createTodoForm = new FormGroup({
    title: new FormControl(''),
    body: new FormControl('')
  })

  onSubmitCreateTodo() {
    this.http.post<Todo>('/api/todos', {
      title: this.createTodoForm.controls.title.value,
      body: this.createTodoForm.controls.body.value
    }).subscribe({
      next: (todo) => this.todos = [...this.todos, todo],
      error: (err) => console.error(err)
    })
  }
}
