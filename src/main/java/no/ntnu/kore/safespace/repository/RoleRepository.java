package no.ntnu.kore.safespace.repository;

import no.ntnu.kore.safespace.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getRoleByNameIgnoreCase(String name);

}
