package com.github.rbaul.clilivetask.backend.web.controlles;

import com.github.rbaul.clilivetask.backend.web.ValidationGroups;
import com.github.rbaul.clilivetask.backend.web.dtos.TaskDto;
import com.github.rbaul.clilivetask.backend.config.ApiImplicitPageable;
import com.github.rbaul.clilivetask.backend.services.TaskService;
import com.github.rbaul.clilivetask.backend.services.impl.TaskMessageServiceImpl;
import com.github.rbaul.clilivetask.backend.web.dtos.errors.ErrorDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("api/task")
public class TaskController {

    private final TaskService taskService;

    private final TaskMessageServiceImpl taskMessageService;

    @ApiOperation(value = "Get Task")
    @ApiResponses({@ApiResponse(code = 200, message = "Retrieved Task"),
            @ApiResponse(code = 404, message = "Task Not Found", response = ErrorDto.class)})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("{taskId}")
    public TaskDto getTask(@PathVariable long taskId) {
        return taskService.get(taskId);
    }

    @ApiOperation(value = "Get All Task")
    @ApiResponses({@ApiResponse(code = 200, message = "Retrieved All Task")})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("all")
    public List<TaskDto> getAllTask() {
        return taskService.getAll();
    }


    @ApiOperation(value = "Create Task")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully Task created"),
            @ApiResponse(code = 428, message = "Invalid Task info", response = ErrorDto.class)})
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public TaskDto create(
            @ApiParam(value = "Task object that needs to be create", name = "TaskDto", required = true)
            @Validated(ValidationGroups.Create.class) @RequestBody TaskDto taskDto) {
        TaskDto taskDtoResponse = taskService.create(taskDto);
        taskMessageService.notifyTasksUpdate();
        return taskDtoResponse;
    }

    @ApiOperation(value = "Update task")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully Task updated"),
            @ApiResponse(code = 428, message = "Invalid task info", response = ErrorDto.class)})
    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping("{taskId}")
    public TaskDto update(@PathVariable long taskId,
                          @ApiParam(value = "Task object that needs to be edit", name = "TaskDto", required = true)
                                   @Validated(ValidationGroups.Create.class) @RequestBody TaskDto taskDto) {
        TaskDto taskDtoResponse = taskService.update(taskId, taskDto);
        taskMessageService.notifyTasksUpdate();
        return taskDtoResponse;
    }

    @ApiOperation(value = "Delete Task")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully Task deleted"),
            @ApiResponse(code = 428, message = "Invalid task Id", response = ErrorDto.class)})
    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping("{taskId}")
    public void delete(@PathVariable long taskId) {
        taskService.delete(taskId);
        taskMessageService.notifyTasksUpdate();
    }

    @ApiOperation(value = "Fetch all tasks with paging")
    @ApiImplicitPageable
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully lists tasks")})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping
    public Page<TaskDto> fetch(@PageableDefault @ApiIgnore(
            "Ignored because swagger ui shows the wrong params, " +
                    "instead they are explained in the implicit params"
    ) Pageable pageable) {
        return taskService.getPageable(pageable);
    }

    @ApiOperation(value = "Get Task Log")
    @ApiResponses({@ApiResponse(code = 200, message = "Retrieved Task log"),
            @ApiResponse(code = 404, message = "Task Not Found", response = ErrorDto.class)})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("log/{taskId}")
    public String getTaskLog(@PathVariable long taskId) {
        return taskService.getLogFileById(taskId);
    }

}
