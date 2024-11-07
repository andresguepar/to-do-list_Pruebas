package co.edu.unihumboldt.to_do_list.controllers;

import co.edu.unihumboldt.to_do_list.domain.enums.Priority;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskDto;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskStatsDto;
import co.edu.unihumboldt.to_do_list.services.impl.TaskServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskServiceImpl service;

    public TaskController(TaskServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> listTasks() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        try {
            TaskDto createdTask = service.save(taskDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable("id") int id) {
        Optional<TaskDto> taskDto = Optional.ofNullable(service.byId(id));
        return taskDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") int id, @RequestBody TaskDto taskDto) {
        Optional<TaskDto> existingTask = Optional.ofNullable(service.byId(id));
        if (existingTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        TaskDto updatedTask = service.save(taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/priority")
    public ResponseEntity<Priority> assignPriority(@RequestParam("limitDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate limitDate) {
        return ResponseEntity.ok(service.assignPriority(limitDate));
    }

    @GetMapping("/focus-mode/{id}")
    public ResponseEntity<Void> enterFocusMode(@PathVariable int id) {
        try {
            service.focusMode(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/focus-mode")
    public ResponseEntity<Void> exitFocusMode() {
        try {
            service.exitFocusMode();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/visible")
    public ResponseEntity<List<TaskDto>> getVisibleTasks() {
        return ResponseEntity.ok(service.getVisibleTasks());
    }

    @GetMapping("/stats")
    public ResponseEntity<TaskStatsDto> getTaskStats() {
        return ResponseEntity.ok(service.getStats());
    }

    @GetMapping("/sorted-by-priority")
    public ResponseEntity<List<TaskDto>> getTasksSortedByPriority() {
        try {
            List<TaskDto> tasks = service.getTasksSortedByPriority();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sorted-by-due-date")
    public ResponseEntity<List<TaskDto>> getTasksSortedByDueDate() {
        try {
            List<TaskDto> tasks = service.getTasksSortedByDueDate();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sorted-by-completion-status")
    public ResponseEntity<List<TaskDto>> getTasksSortedByCompletionStatus() {
        try {
            List<TaskDto> sortedTasks = service.getTasksSortedByCompletionStatus();
            return ResponseEntity.ok(sortedTasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/toggle-complete")
    public ResponseEntity<TaskDto> toggleComplete(@PathVariable("id") Integer id) {
        try {
            TaskDto task = service.byId(id);
            task.setCompleted(!task.isCompleted());
            if (task.isCompleted()) {
                task.setCompletedDate(LocalDate.now());
            } else {
                task.setCompletedDate(null);
            }
            TaskDto updatedTask = service.save(task);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}