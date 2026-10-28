package taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanagement.entity.Task;

import java.util.List;

public interface TasksRepository extends JpaRepository <Task, Long> {

    List<Task> findAllByOrderByIdDesc();

    List<Task> findAllByAuthor_EmailIgnoreCaseOrderByIdDesc(String email);
}
