import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TasksTableComponent } from './tasks-table/tasks-table.component';
import { MatTableModule, MatPaginatorModule, MatSortModule, MatToolbarModule,
  MatDialogModule, MatButtonModule, MatTooltipModule, MatInputModule, MatOptionModule,
   MatSelectModule, MatIconModule, MatSlideToggleModule } from '@angular/material';
import { HttpClientModule } from '@angular/common/http';
import { TaskDialogComponent } from './task-dialog/task-dialog.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ConfirmationDialogComponent } from './confirmation-dialog/confirmation-dialog.component';
import { TaskLogDialogComponent } from './task-log-dialog/task-log-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    TasksTableComponent,
    TaskDialogComponent,
    ConfirmationDialogComponent,
    TaskLogDialogComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatToolbarModule,
    MatDialogModule,
    MatButtonModule,
    MatTooltipModule,
    MatInputModule,
    MatOptionModule,
    MatSelectModule,
    MatIconModule,
    MatSlideToggleModule
  ],
  bootstrap: [AppComponent],
  entryComponents: [TaskDialogComponent, ConfirmationDialogComponent, TaskLogDialogComponent]
})
export class AppModule { }
