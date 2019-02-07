import { Component, OnInit, ViewChild, OnDestroy, AfterViewInit } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material';
import { TaskService } from '../services/task.service';
import { Task } from '../models/task';
import { TaskDialogComponent } from '../task-dialog/task-dialog.component';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { TaskLogDialogComponent } from '../task-log-dialog/task-log-dialog.component';

@Component({
  selector: 'app-tasks-table',
  templateUrl: './tasks-table.component.html',
  styleUrls: ['./tasks-table.component.scss']
})
export class TasksTableComponent implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<Task> = new MatTableDataSource();

  private socketUrl = '/task-socket';

  private stompClient;

  checked = false;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'createdDate', 'name', 'description', 'command', 'state', 'actions'];

  constructor(
    private dialog: MatDialog,
    private taskApiService: TaskService
    ) {
      this.connect();
  }

  ngOnInit() {
    this.taskApiService.getAllTasks().subscribe(tasks => {
      this.dataSource.data = tasks;
    });

  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  connect() {
    const socket = new SockJS(this.socketUrl);
    this.stompClient = Stomp.over(socket);
    const _this = this;
    this.stompClient.connect({}, function(frame) {
      _this.stompClient.subscribe('/topic/tasks-update', (message) => {
        if (message.body) {
          const tasks: Task[] = JSON.parse(message.body);
          console.log('Received from topic "/topic/tasks-update": ', tasks);
          _this.dataSource.data = tasks;
        }
      });
    });

  }

  disconnect() {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim().toLocaleLowerCase();
    this.dataSource.filter = filterValue;
  }

  ngOnDestroy(): void {
    this.disconnect();
  }

  openConfirmDialog(): MatDialogRef<ConfirmationDialogComponent> {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    dialogConfig.data = {
      message: 'Are you sure to delete this task?',
      okName: 'Delete',
      title: 'Deletion'
    };

    return this.dialog.open(ConfirmationDialogComponent, dialogConfig);
  }

  openTaskLogDialog(taskData: Task): MatDialogRef<TaskLogDialogComponent> {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '85vh';
    dialogConfig.width = '100vh';

    dialogConfig.data = taskData;

    return this.dialog.open(TaskLogDialogComponent, dialogConfig);
  }

  openTaskDialog(taskData: Task): MatDialogRef<TaskDialogComponent> {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    dialogConfig.data = taskData;

    return this.dialog.open(TaskDialogComponent, dialogConfig);
  }

  createTask() {
    this.openTaskDialog(new Task()).afterClosed().subscribe(
      data => {
        if (data !== undefined) {
          this.taskApiService.addTask(data).subscribe();
        }
      }
    );
  }

  editTask(task: Task) {
    this.openTaskDialog(task).afterClosed().subscribe(
      data => {
        if (data !== undefined) {
          this.taskApiService.updateTask(task.id, data)
        .subscribe(response => console.log(response));
        }
      }
    );
  }

  openTaskLog(task: Task) {
    this.openTaskLogDialog(task);
  }

  deleteTask(task: Task) {
    this.openConfirmDialog().afterClosed().subscribe(
      data => {
        if (data !== undefined) {
          this.taskApiService.deleteTask(task.id)
        .subscribe(response => console.log(response));
        }
      }
    );
  }

}
