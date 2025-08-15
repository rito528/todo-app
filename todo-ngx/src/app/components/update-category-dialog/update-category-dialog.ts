import { Component, inject, model } from '@angular/core';
import { Category } from '../../../types';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';


@Component({
  selector: 'update-category-dialog',
  templateUrl: 'update-category-dialog.html',
  styleUrl: 'update-category-dialog.scss',
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
export class UpdateCategoryDialog {
  readonly dialogRef = inject(MatDialogRef<UpdateCategoryDialog>)
  readonly data = inject<Category>(MAT_DIALOG_DATA)
  readonly category = model(this.data)

  onCancel(): void {
    this.dialogRef.close();
  }
}
