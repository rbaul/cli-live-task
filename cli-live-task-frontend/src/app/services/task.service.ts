import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Task } from '../models/task';
import { Observable } from 'rxjs';

const API_URL = '/api/task/';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private http: HttpClient) { }

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(API_URL);
  }

  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(API_URL + 'all');
  }

  getTask(id: number): Observable<Task> {
    return this.http.get<Task>(API_URL + id.toString());
  }

  addTask(task: Task): Observable<Task> {
    return this.http.post<Task>(API_URL, task, httpOptions);
  }

  updateTask(id: number, task: Task): Observable<any> {
    return this.http.put(API_URL + id.toString(), task, httpOptions);
  }

  deleteTask(id: number): Observable<Task> {
    return this.http.delete<Task>(API_URL + id.toString(), httpOptions);
  }

  getTaskLog(id: number): Observable<String> {
    return this.http.get(API_URL + 'log/' + id.toString(), {responseType: 'text'});
  }
}
