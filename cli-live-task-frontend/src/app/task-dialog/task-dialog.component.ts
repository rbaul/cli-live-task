import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-task-dialog',
  templateUrl: './task-dialog.component.html',
  styleUrls: ['./task-dialog.component.scss']
})
export class TaskDialogComponent implements OnInit {

  form: FormGroup;

  name: string;
  description: string;
  command: string;

  constructor(
      private fb: FormBuilder,
      private dialogRef: MatDialogRef<TaskDialogComponent>,
      @Inject(MAT_DIALOG_DATA) data) {

      this.name = data.name;
      this.description = data.description;
      this.command = data.command;
  }

  ngOnInit() {
    this.form = this.fb.group({
      name: [this.name, [Validators.required]],
      description: [this.description, [Validators.required]],
      command: [this.command, [Validators.required]]
    });
  }

  save() {
      this.dialogRef.close(this.form.value);
  }

  close() {
      this.dialogRef.close();
  }

}
