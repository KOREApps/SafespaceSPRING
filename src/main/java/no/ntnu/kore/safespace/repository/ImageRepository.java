package no.ntnu.kore.safespace.repository;

import no.ntnu.kore.safespace.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> { }
