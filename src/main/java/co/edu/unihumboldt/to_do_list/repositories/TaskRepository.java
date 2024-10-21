package co.edu.unihumboldt.to_do_list.repositories;

import co.edu.unihumboldt.to_do_list.domain.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Interfaz que extiende JpaRepository para la gestión de entidades Task en la base de datos.
 * Proporciona métodos CRUD y funcionalidades adicionales para la manipulación de tareas,
 * aprovechando las capacidades de Spring Data JPA.
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

}
