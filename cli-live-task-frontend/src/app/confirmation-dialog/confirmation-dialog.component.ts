import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent implements OnInit {

  data: ConfirmationDialogData = {};

  constructor(
    private dialogRef: MatDialogRef<ConfirmationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data) {
    this.data.message = (data && data.message) ? data.message : 'Are you sure?';
    this.data.okName = (data && data.okName) ? data.okName : 'Ok';
    this.data.title = (data && data.title) ? data.title : 'Confirmation';
  }

  ngOnInit() {
  }

  ok() {
    this.dialogRef.close(true);
  }

  close() {
    this.dialogRef.close();
  }

}

export interface ConfirmationDialogData {
  message?: string;
  okName?: string;
  title?: string;
}
