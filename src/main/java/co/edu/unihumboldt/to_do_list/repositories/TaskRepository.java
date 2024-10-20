package co.edu.unihumboldt.to_do_list.repositories;

import co.edu.unihumboldt.to_do_list.domain.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

}
