package no.ntnu.kore.safespace.repository;

import no.ntnu.kore.safespace.entity.BugReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugReportRepository extends JpaRepository<BugReport, Long> { }
