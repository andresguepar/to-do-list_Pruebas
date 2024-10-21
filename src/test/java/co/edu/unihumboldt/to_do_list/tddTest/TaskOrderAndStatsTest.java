package co.edu.unihumboldt.to_do_list.tddTest;

import co.edu.unihumboldt.to_do_list.domain.entities.Task;
import co.edu.unihumboldt.to_do_list.domain.enums.Priority;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskDto;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskStatsDto;
import co.edu.unihumboldt.to_do_list.repositories.TaskRepository;
import co.edu.unihumboldt.to_do_list.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class TaskOrderAndStatsTest {
    @Mock
    private TaskRepository repository;

    private TaskServiceImpl taskService;

    private Task studyTask;
    private Task watchSeriesTask;
    private Task overdueTask1;
    private Task overdueTask2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskServiceImpl(repository);

        // Setup test tasks usando el constructor completo
        studyTask = new Task(
                1,                          // id
                "Estudiar",                 // title
                "Estudiar para el examen",  // description
                Priority.HIGH,              // priority
                LocalDate.now(),            // creationDate
                LocalDate.of(2024, 10, 21), // limitDate
                false,                      // completed
                null,                       // completedDate
                1                           // rewardLevel
        );

        watchSeriesTask = new Task(
                2,                          // id
                "Ver series",               // title
                "Maratón de series",        // description
                Priority.LOW,               // priority
                LocalDate.now(),            // creationDate
                LocalDate.of(2024, 10, 22), // limitDate
                false,                      // completed
                null,                       // completedDate
                1                           // rewardLevel
        );

        // Setup overdue tasks
        overdueTask1 = new Task(
                3,                                    // id
                "Tarea vencida 1",                   // title
                "Primera tarea vencida",             // description
                Priority.HIGH,                       // priority
                LocalDate.now(),                     // creationDate
                LocalDate.now().minusDays(5),        // limitDate
                false,                               // completed
                null,                                // completedDate
                1                                    // rewardLevel
        );

        overdueTask2 = new Task(
                4,                                    // id
                "Tarea vencida 2",                   // title
                "Segunda tarea vencida",             // description
                Priority.MEDIUM,                     // priority
                LocalDate.now(),                     // creationDate
                LocalDate.now().minusDays(2),        // limitDate
                false,                               // completed
                null,                                // completedDate
                1                                    // rewardLevel
        );
    }

    @Test
    @DisplayName("CP-02-TDD: Deberia devolver las estadisticas de las tareas correctamente")
    void shouldReturnCorrectTaskStatistics() {
        // Arrange
        List<Task> allTasks = Arrays.asList(
                studyTask,
                watchSeriesTask,
                overdueTask1,
                overdueTask2,
                new Task(5, "Completed1", "Tarea completada 1", Priority.LOW,
                        LocalDate.now(), LocalDate.now(), true, LocalDate.now(), 1),
                new Task(6, "Completed2", "Tarea completada 2", Priority.LOW,
                        LocalDate.now(), LocalDate.now(), true, LocalDate.now(), 1),
                new Task(7, "Completed3", "Tarea completada 3", Priority.LOW,
                        LocalDate.now(), LocalDate.now(), true, LocalDate.now(), 1)
        );

        when(repository.findAll()).thenReturn(allTasks);

        // Act
        TaskStatsDto stats = taskService.getStats();

        // Assert
        assertEquals(7, stats.getTotalTasks());
        assertEquals(3, stats.getCompletedTasks());
        assertEquals(4, stats.getPendingTasks());
        assertEquals(2, stats.getOverDueTasks());
    }

    @Test
    @DisplayName("CP-02-TDD: Deberia ordenar la lista de tareas por prioridad")
    void shouldSortTasksByPriority() {
        // Arrange
        List<Task> tasks = Arrays.asList(watchSeriesTask, studyTask);
        when(repository.findAll()).thenReturn(tasks);

        // Act
        List<TaskDto> sortedTasks = taskService.getTasksSortedByPriority();

        // Assert
        assertEquals(2, sortedTasks.size());
        assertEquals("Estudiar", sortedTasks.get(0).getTitle()); // High priority should be first
        assertEquals("Ver series", sortedTasks.get(1).getTitle()); // Low priority should be second
    }

    @Test
    @DisplayName("CP-02-TDD: Deberia ordenar la lista de tareas por fecha")
    void shouldSortTasksByDueDate() {
        // Arrange
        List<Task> tasks = Arrays.asList(watchSeriesTask, studyTask);
        when(repository.findAll()).thenReturn(tasks);

        // Act
        List<TaskDto> sortedTasks = taskService.getTasksSortedByDueDate();

        // Assert
        assertEquals(2, sortedTasks.size());
        assertEquals("Estudiar", sortedTasks.get(0).getTitle()); // Earlier date should be first
        assertEquals("Ver series", sortedTasks.get(1).getTitle()); // Later date should be second
    }

}
