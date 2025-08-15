import { Component, inject } from "@angular/core";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatTableModule } from "@angular/material/table";
import { TodoStatePipe } from "../../pipes/todo-state-pipe";
import { Category, Todo } from "../../../types";
import { HttpClient } from "@angular/common/http";
import { MatDialog } from "@angular/material/dialog";
import { todoUpdateFormSchema, UpdateTodoDialog } from "../../update-dialog/update-todo-dialog";
import { MatMenuModule } from "@angular/material/menu";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";

@Component({
  selector: 'todo-table',
  templateUrl: 'todo-table.html',
  imports: [
    MatTableModule,
    MatFormFieldModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule,
    TodoStatePipe
  ]
})
export class TodoTable {
  private http = inject(HttpClient)
  readonly dialog = inject(MatDialog)

  todos: Todo[] = []
  categories: Category[] = []
  
  displayedColumns: string[] = ["title", "body", "state", "category", "operation"]

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
