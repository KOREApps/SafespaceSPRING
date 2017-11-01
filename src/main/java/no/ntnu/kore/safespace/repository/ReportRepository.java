package no.ntnu.kore.safespace.repository;

import no.ntnu.kore.safespace.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
