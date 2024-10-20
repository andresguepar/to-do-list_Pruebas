package co.edu.unihumboldt.to_do_list.services;

import co.edu.unihumboldt.to_do_list.domain.enums.Priority;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskDto;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskStatsDto;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    List<TaskDto> list();
    TaskDto byId(int id);
    void save(TaskDto t);
    void update(int id, TaskDto updated);
    void delete(int id);
    Priority assignPriority(LocalDate limitDate);

    TaskDto focusMode(int id);

    List<TaskDto> getVisibleTasks();

    void exitFocusMode();

    TaskStatsDto getStats();

    List<TaskDto> getTasksSortedByPriority();

    List<TaskDto> getTasksSortedByDueDate();

    List<TaskDto> getTasksSortedByCompletionStatus();
}
