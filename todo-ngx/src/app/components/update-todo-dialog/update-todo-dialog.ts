import { Component, inject, model } from '@angular/core';
import { categorySchema, todoStateSchema } from '../../../types';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog';
import { z } from 'zod';
import { MatButtonModule } from '@angular/material/button';

export const todoUpdateFormSchema = z.object({
  categories: categorySchema.array(),
  todo: z.object({
    categoryId: z.uint32().nullable(),
    title: z.string().max(255),
    body: z.string(),
    state: todoStateSchema
  })
})

type TodoUpdateForm = z.infer<typeof todoUpdateFormSchema>

@Component({
  selector: 'update-todo-dialog',
  templateUrl: 'update-todo-dialog.html',
  styleUrl: 'update-todo-dialog.scss',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    FormsModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatButtonModule,
  ]
})
export class UpdateTodoDialog {
  readonly dialogRef = inject(MatDialogRef<UpdateTodoDialog>)
  readonly data = inject<TodoUpdateForm>(MAT_DIALOG_DATA)
  readonly todo = model(this.data)

  onCancel(): void {
    this.dialogRef.close();
  }
}
