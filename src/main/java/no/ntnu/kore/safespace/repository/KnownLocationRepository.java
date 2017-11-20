package no.ntnu.kore.safespace.repository;

import no.ntnu.kore.safespace.entity.KnownLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KnownLocationRepository extends JpaRepository<KnownLocation, Long> {

    KnownLocation findByNameEqualsIgnoreCase(String name);

    List<KnownLocation> findAllByNameEqualsIgnoreCase(String name);

}
