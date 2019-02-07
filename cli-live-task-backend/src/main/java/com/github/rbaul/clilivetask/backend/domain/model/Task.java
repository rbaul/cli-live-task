package com.github.rbaul.clilivetask.backend.domain.model;

import com.github.rbaul.clilivetask.backend.domain.model.types.TaskState;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
public class Task extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String command;

    @Enumerated(EnumType.STRING)
    private TaskState state;
}
