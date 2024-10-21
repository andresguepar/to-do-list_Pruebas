package co.edu.unihumboldt.to_do_list.services.impl;

import co.edu.unihumboldt.to_do_list.domain.entities.Task;
import co.edu.unihumboldt.to_do_list.domain.enums.Priority;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskDto;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskStatsDto;
import co.edu.unihumboldt.to_do_list.mapping.mappers.TaskMapper;
import co.edu.unihumboldt.to_do_list.repositories.TaskRepository;
import co.edu.unihumboldt.to_do_list.services.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * Implementación del servicio de tareas, que proporciona la lógica de negocio
 * para la gestión de tareas. Incluye métodos para listar, obtener por ID, guardar,
 * actualizar y eliminar tareas. También maneja la asignación de prioridades basadas
 * en fechas límite y proporciona estadísticas sobre las tareas. Soporta un modo
 * de enfoque para visualizar una tarea específica y permite la obtención de tareas
 * ordenadas por prioridad, fecha de vencimiento y estado de completado.
 */

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;

    private boolean isInFocusMode = false;
    private Integer focusTaskId = null;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<TaskDto> list() {
        List<Task> Task = repository.findAll();
        return TaskMapper.mapFromDto(Task);
    }

    @Override
    public TaskDto byId(int id) {
        Task Task = repository.findById(id).orElseThrow();
        return TaskMapper.mapFrom(Task);
    }

    @Override
    public void save(TaskDto t) {
        if (t.getTitle() == null || t.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be null or empty");
        }

        if (t.getCreationDate() == null) {
            t.setCreationDate(LocalDate.now());
        }
        if (t.getPriority() == null) {
            t.setPriority(assignPriority(t.getLimitDate()));
        }

        Task Task = TaskMapper.mapFrom(t);
        repository.save(Task);
    }

    @Override
    public void update(int id, TaskDto update) {
        Task task = repository.findById(id).orElseThrow();
        Task updated = TaskMapper.mapFrom(update);

        task.setTitle(updated.getTitle());
        task.setDescription(updated.getDescription());
        task.setCreationDate(updated.getCreationDate());
        task.setLimitDate(updated.getLimitDate());
        task.setCompleted(updated.isCompleted());
        task.setCompletedDate(updated.getCompletedDate());
        task.setRewardLevel(updated.getRewardLevel());

        repository.save(task);
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public Priority assignPriority(LocalDate limitDate) {
        if (limitDate == null) {
            return Priority.LOW;
        }

        LocalDate currentDate = LocalDate.now();

        if (limitDate.isBefore(currentDate)) {
            return Priority.HIGH;
        }

        long deadline = ChronoUnit.DAYS.between(currentDate, limitDate);

        if (deadline <= 3) {
            return Priority.HIGH;
        } else if (deadline <= 7) {
            return Priority.MEDIUM;
        } else {
            return Priority.LOW;
        }
    }

    @Override
    public TaskDto focusMode(int id) {
        Task task = repository.findById(id).orElseThrow();

        this.isInFocusMode = true;
        this.focusTaskId = id;
        return TaskMapper.mapFrom(task);
    }

    @Override
    public List<TaskDto> getVisibleTasks() {

        if (isInFocusMode && focusTaskId != null){
            return repository.findById(focusTaskId)
                    .map(task -> List.of(TaskMapper.mapFrom(task)))
                    .orElseGet(Collections::emptyList);

        }
        return repository.findAll().stream()
                .map(TaskMapper::mapFrom)
                .toList();
    }

    @Override
    public void exitFocusMode(){
        isInFocusMode = false;
        focusTaskId = null;
    }

    @Override
    public TaskStatsDto getStats() {
        List<Task> allTasks = repository.findAll();
        TaskStatsDto stats = new TaskStatsDto();

        stats.setTotalTasks(allTasks.size());
        stats.setCompletedTasks(allTasks.stream().filter(Task::isCompleted).count());
        stats.setPendingTasks(allTasks.stream().filter(t -> !t.isCompleted()).count());

        stats.setHighPriorityTasks(allTasks.stream()
                .filter(t->t.getPriority() == Priority.HIGH).count());
        stats.setMediumPriorityTasks(allTasks.stream()
                .filter(t->t.getPriority() ==Priority.MEDIUM).count());
        stats.setMediumPriorityTasks(allTasks.stream()
                .filter(t->t.getPriority() ==Priority.LOW).count());

        if(!allTasks.isEmpty()){
            stats.setCompletionRate((double) stats.getCompletedTasks() / stats.getTotalTasks());
            stats.setAverageRewardLevel(allTasks.stream()
                    .mapToInt(Task::getRewardLevel)
                    .average()
                    .orElse(0.0));
        }

        LocalDate today = LocalDate.now();
        stats.setTaskCompletedOnTime(allTasks.stream()
                .filter(t -> t.isCompleted() &&
                        t.getCompletedDate() != null &&
                        !t.getCompletedDate().isAfter(t.getLimitDate()))
                .count());
        stats.setOverDueTasks(allTasks.stream()
                .filter(t -> !t.isCompleted() &&
                        t.getLimitDate().isBefore(today))
                .count());
        return stats;
    }
    @Override
    public List<TaskDto> getTasksSortedByPriority() {
        return repository.findAll().stream()
                .sorted(Comparator
                        .comparing(Task::getPriority,Comparator.nullsFirst(Comparator.naturalOrder()))
                        .thenComparing(Task::getLimitDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                        .thenComparing(Task::getCreationDate, Comparator.nullsFirst(Comparator.naturalOrder())))
                .map(TaskMapper::mapFrom)
                .toList();
    }

    @Override
    public List<TaskDto> getTasksSortedByDueDate() {
        return repository.findAll().stream()
                .sorted(Comparator
                        .comparing(Task::getLimitDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                        .thenComparing(Task::getPriority, Comparator.nullsFirst(Comparator.naturalOrder()))
                        .thenComparing(Task::getCreationDate, Comparator.nullsFirst(Comparator.naturalOrder())))
                .map(TaskMapper::mapFrom)
                .toList();
    }

    @Override
    public List<TaskDto> getTasksSortedByCompletionStatus() {
        return repository.findAll().stream()
                .sorted(Comparator
                        .comparing(Task::isCompleted, Comparator.nullsFirst(Comparator.naturalOrder()))
                        .thenComparing(Task::getPriority, Comparator.nullsFirst(Comparator.naturalOrder()))
                        .thenComparing(Task::getLimitDate, Comparator.nullsFirst(Comparator.naturalOrder())))
                .map(TaskMapper::mapFrom)
                .toList();

    }
}
