package dev.matheuslf.desafio.inscritos.repositories;

import dev.matheuslf.desafio.inscritos.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
