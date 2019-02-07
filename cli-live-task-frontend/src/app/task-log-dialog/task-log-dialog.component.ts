import { Component, OnInit, Inject, OnDestroy } from '@angular/core';
import { Validators, FormBuilder, FormGroup, FormControl, AbstractControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { TaskService } from '../services/task.service';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { TaskState } from '../models/task';

@Component({
  selector: 'app-task-log-dialog',
  templateUrl: './task-log-dialog.component.html',
  styleUrls: ['./task-log-dialog.component.scss']
})
export class TaskLogDialogComponent implements OnInit, OnDestroy {

  form: FormGroup;

  id: number;
  name: string;
  description: string;
  state: string;
  command: string;

  private socketUrl = '/task-socket';
  stompClient: any;

  constructor(
      private fb: FormBuilder,
      private taskApiService: TaskService,
      private dialogRef: MatDialogRef<TaskLogDialogComponent>,
      @Inject(MAT_DIALOG_DATA) data) {

      this.id = data.id;
      this.name = data.name;
      this.description = data.description;
      this.state = data.state;
      this.command = data.command;
  }

  ngOnInit() {
    this.form = this.fb.group({
      name: {value: this.name, disabled: true},
      description: {value: this.description, disabled: true},
      command: {value: this.command, disabled: true},
      log: {value: '', disabled: true}
    });

    this.taskApiService.getTaskLog(this.id).subscribe(log => {
      const logCtrl: AbstractControl = this.form.controls['log'];
      logCtrl.patchValue(log);
      logCtrl.updateValueAndValidity();

      if (this.state === TaskState[TaskState.EXECUTING]) {
        this.connect();
      }
    }, error => console.log(error));
  }


  connect() {
    const socket = new SockJS(this.socketUrl);
    this.stompClient = Stomp.over(socket);
    const _this = this;
    this.stompClient.connect({}, function(frame) {
      _this.stompClient.subscribe(`/topic/task-progress-log-update/${_this.id}`, (message) => {
        if (message.body) {
          const progressLog: string = message.body;
          console.log(`Received from topic "/topic/task-progress-log-update/${_this.id}": `, progressLog);
          const logCtrl: AbstractControl = _this.form.controls['log'];
          const newLog = logCtrl.value + progressLog;
          logCtrl.patchValue(newLog);
        }
      });
    });

  }

  disconnect() {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }
  }

  ngOnDestroy(): void {
    this.disconnect();
  }


  close() {
      this.dialogRef.close();
  }

}
