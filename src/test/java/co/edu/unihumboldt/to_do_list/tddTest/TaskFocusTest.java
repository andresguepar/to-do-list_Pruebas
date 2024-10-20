package co.edu.unihumboldt.to_do_list.tddTest;

import co.edu.unihumboldt.to_do_list.domain.entities.Task;
import co.edu.unihumboldt.to_do_list.domain.enums.Priority;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskDto;
import co.edu.unihumboldt.to_do_list.repositories.TaskRepository;
import co.edu.unihumboldt.to_do_list.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskFocusTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskServiceImpl service;

    private Task doHomework;
    private List<Task> multipleTasks;

    @BeforeEach
    void setup(){
        doHomework = new Task();
        doHomework.setId(1);
        doHomework.setTitle("Hacer tarea");
        doHomework.setDescription("Libro ingles");
        doHomework.setPriority(Priority.HIGH);
        doHomework.setCreationDate(LocalDate.now());
        doHomework.setLimitDate(LocalDate.now().plusDays(1));
        doHomework.setCompleted(false);
        doHomework.setRewardLevel(3);

        Task otherHomework = new Task();
        otherHomework.setId(2);
        otherHomework.setTitle("Hacer otra tarea");
        otherHomework.setDescription("si");
        otherHomework.setPriority(Priority.MEDIUM);
        otherHomework.setCreationDate(LocalDate.now());
        otherHomework.setCompleted(false);

        multipleTasks = Arrays.asList(doHomework,otherHomework);
    }

    @Test
    @DisplayName("Al entrar en modo enfocado, solo se debe mostrar la tarea seleccionada")
    void ShowSelectedTask(){
        when(repository.findById(1)).thenReturn(Optional.of(doHomework));

        TaskDto focusedTask = service.focusMode(1);
        List<TaskDto> visibleTasks = service.getVisibleTasks();

        assertNotNull(focusedTask, "La tarea seleccionada no debe ser null");
        assertEquals("Hacer tarea", focusedTask.title(),
                "La tarea enfocada debe ser Hacer Tarea");
        assertEquals(1,visibleTasks.size(),
                "Solo debe mostrase la tarea seleccionada");
        assertEquals(focusedTask.id(), visibleTasks.get(0).id(),
                "La tarea visible debe ser la tarea seleccionada");
    }


}
