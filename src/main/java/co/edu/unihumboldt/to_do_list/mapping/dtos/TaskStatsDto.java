package co.edu.unihumboldt.to_do_list.mapping.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase DTO (Data Transfer Object) que representa estadísticas de tareas en la aplicación.
 * Incluye información sobre el total de tareas, tareas completadas y pendientes,
 * cantidad de tareas por prioridad, tasa de finalización, tareas completadas a tiempo,
 * tareas vencidas y nivel promedio de recompensa. Utiliza anotaciones Lombok para la
 * generación automática de métodos.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatsDto {
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long highPriorityTasks;
    private long mediumPriorityTasks;
    private long lowPriorityTasks;
    private double completionRate;
    private long taskCompletedOnTime;
    private long overDueTasks;
    private double averageRewardLevel;
}
