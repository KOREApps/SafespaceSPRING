package no.ntnu.kore.safespace.repository;

import no.ntnu.kore.safespace.entity.Documentation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentationRepository extends JpaRepository<Documentation, Long> {
}
