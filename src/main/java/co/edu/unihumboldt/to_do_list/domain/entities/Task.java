package co.edu.unihumboldt.to_do_list.domain.entities;

import co.edu.unihumboldt.to_do_list.domain.enums.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
/**
 * Clase que representa una tarea en la base de datos.
 * Contiene atributos como id, título, descripción, prioridad, fechas de creación y límite,
 * estado de completado y nivel de recompensa. Utiliza anotaciones JPA para mapeo a la tabla
 * "tasks" en el esquema "public". Se incluye soporte para construcción de objetos con el patrón Builder.
 */

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
