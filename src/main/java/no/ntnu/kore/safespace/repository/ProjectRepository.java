package no.ntnu.kore.safespace.repository;

import no.ntnu.kore.safespace.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
