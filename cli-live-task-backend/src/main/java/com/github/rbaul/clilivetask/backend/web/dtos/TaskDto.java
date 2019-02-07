package com.github.rbaul.clilivetask.backend.web.dtos;

import com.github.rbaul.clilivetask.backend.domain.model.types.TaskState;
import com.github.rbaul.clilivetask.backend.web.ValidationGroups;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.Null;

@ApiModel("Task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TaskDto extends AuditableDto {

    @Null(groups = ValidationGroups.Create.class)
    private Long id;

    private String name;

    private String description;

    private String command;

    @Null
    private TaskState state = TaskState.CREATED;
}
