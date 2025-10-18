package dev.matheuslf.desafio.inscritos.repositories;

import dev.matheuslf.desafio.inscritos.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
