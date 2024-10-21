package co.edu.unihumboldt.to_do_list.domain.entities;

import co.edu.unihumboldt.to_do_list.domain.enums.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tasks",schema = "public", indexes = @Index(columnList = "id"))

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private LocalDate creationDate;
    private LocalDate limitDate;
    private boolean completed;
    private LocalDate completedDate;
    private int rewardLevel;
}
