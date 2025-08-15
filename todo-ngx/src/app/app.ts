import { Component, inject, Injectable } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Category, Todo } from '../types';
import { MatDialog } from '@angular/material/dialog';
import { TodoTable } from './components/todo-table/todo-table';
import { CreateTodoForm } from './components/create-todo-form/create-todo-form';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    TodoTable,
    CreateTodoForm
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
}
