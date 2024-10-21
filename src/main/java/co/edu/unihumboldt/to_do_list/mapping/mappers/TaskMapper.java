package co.edu.unihumboldt.to_do_list.mapping.mappers;

import co.edu.unihumboldt.to_do_list.domain.entities.Task;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskDto;

import java.util.List;
/**
 * Clase encargada de mapear entre las entidades Task y TaskDto, facilitando la
 * conversión de datos entre estas dos representaciones. Incluye métodos para
 * convertir de Task a TaskDto y viceversa, así como para listas de ambas clases.
 * Utiliza streams paralelos para mejorar el rendimiento en las conversiones de listas.
 */

public class TaskMapper {
    public static TaskDto mapFrom(Task source){
        return TaskDto.builder()
                .id(source.getId())
                .title(source.getTitle())
                .description(source.getDescription())
                .priority(source.getPriority())
                .creationDate(source.getCreationDate())
                .limitDate(source.getLimitDate())
                .completed(source.isCompleted())
                .rewardLevel(source.getRewardLevel())
                .build();
    }

    public static Task mapFrom(TaskDto source){
        return new Task(source.getId(),
                source.getTitle(),
                source.getDescription(),
                source.getPriority(),
                source.getCreationDate(),
                source.getLimitDate(),
                source.isCompleted(),
                source.getCompletedDate(),
                source.getRewardLevel()

           );
    }

    public static List<Task> mapFrom(List<TaskDto> source){
        return source.parallelStream()
                .map(TaskMapper::mapFrom)
                .toList();

    }
    public static List<TaskDto> mapFromDto(List<Task> source){
        return source.parallelStream()
                .map(TaskMapper::mapFrom)
                .toList();

    }
}
