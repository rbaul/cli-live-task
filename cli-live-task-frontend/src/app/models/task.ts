export class Task {
    id: number;
    name: string;
    description: string;
    command: string;
    state: TaskState;
    createdDate: string;
}

export enum TaskState {
    EXECUTING, DONE, CREATED, FAILED
}
