import { Component, inject, Injectable } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Category, Todo } from '../types';
import { MatDialog } from '@angular/material/dialog';
import { TodoTable } from './components/todo-table/todo-table';
import { CreateTodoForm } from './components/create-todo-form/create-todo-form';
import { MatTabsModule } from '@angular/material/tabs';
import { CategoryTable } from './components/category-table/category-table';
import { CreateCategoryForm } from './components/create-category-form/create-category-form';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    MatTabsModule,
    TodoTable,
    CreateTodoForm,
    CategoryTable,
    CreateCategoryForm,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
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

  createTodo(todo: Todo) {
    this.todos = [...this.todos, todo] 
  }

  createCategory(category: Category) {
    this.categories = [...this.categories, category]
  }

  putCategories(categories: Category[]) {
    this.categories = categories
    this.todos = this.todos.map(todo => {
      const category = this.categories.find(category => category.id === todo.category?.id)
      
      if (category === undefined) {
        return {
          ...todo,
          category: null
        }
      } else {
        return todo
      }
    })
  }
}
