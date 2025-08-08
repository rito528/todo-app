import { Component, inject, Injectable, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatTableModule } from '@angular/material/table';
import { Todo } from '../types';
import { TodoStatePipe } from './pipes/todo-state-pipe';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MatTableModule, TodoStatePipe],
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

  displayedColumns: string[] = ["title", "body", "state"]

}
