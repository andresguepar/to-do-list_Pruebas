package co.edu.unihumboldt.to_do_list.tddTest;

import co.edu.unihumboldt.to_do_list.domain.entities.Task;
import co.edu.unihumboldt.to_do_list.domain.enums.Priority;
import co.edu.unihumboldt.to_do_list.repositories.TaskRepository;
import co.edu.unihumboldt.to_do_list.services.TaskService;
import co.edu.unihumboldt.to_do_list.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskPriorityTest {

    private TaskService service;
    private TaskRepository repository;

    @BeforeEach
    void setup(){
        service = new TaskServiceImpl(repository);
    }

    @Test
    @DisplayName("Al no asignar una prioridad se asigna una respecto al tiempo")
    void assignPriorityOnSpecificDate(){
        LocalDate deadLine = LocalDate.of(2024,10,23);
        LocalDate currentDate = LocalDate.now();

        Task task = Task.builder()
                .title("Preparar Presentacion")
                .description("Tiene que tener imagenes")
                .limitDate(deadLine)
                .creationDate(currentDate)
                .build();

        Priority assignedPriority = service.assignPriority(task.getLimitDate());
        task.setPriority(assignedPriority);

        Priority expectedPriority = calculatePriority(currentDate, deadLine);

        assertEquals(expectedPriority, task.getPriority(),
            String.format("Para la fecha limite %s, desde la fecha actual %s, la prioridad deberia ser %s",
                    deadLine, currentDate, expectedPriority));
    }

    private Priority calculatePriority(LocalDate currentDate, LocalDate deadLine){
        long daysUntilDeadLine = ChronoUnit.DAYS.between(currentDate, deadLine);

        if(daysUntilDeadLine <= 3){
            return Priority.HIGH;
        }else if (daysUntilDeadLine <= 7){
            return Priority.MEDIUM;
        }else {
            return Priority.LOW;
        }

    }
}
