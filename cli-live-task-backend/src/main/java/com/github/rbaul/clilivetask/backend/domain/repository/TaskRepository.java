package com.github.rbaul.clilivetask.backend.domain.repository;

import com.github.rbaul.clilivetask.backend.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
