package co.edu.unihumboldt.to_do_list.mapping.dtos;

import co.edu.unihumboldt.to_do_list.domain.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private int id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDate creationDate;
    private LocalDate limitDate;
    private boolean completed;
    private LocalDate completedDate;
    private int rewardLevel;
}
