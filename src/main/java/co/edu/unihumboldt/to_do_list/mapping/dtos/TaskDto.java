package co.edu.unihumboldt.to_do_list.mapping.dtos;

import co.edu.unihumboldt.to_do_list.domain.enums.Priority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Builder
public record TaskDto (
     int id,
     String title,
     String description,
     Priority priority,
     LocalDate creationDate,
     LocalDate limitDate,
     boolean completed,
     LocalDate completedDate,
     int rewardLevel){
}
