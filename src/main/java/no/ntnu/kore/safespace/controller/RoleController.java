package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.Role;
import no.ntnu.kore.safespace.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("roles")
public class RoleController implements RestService<Role, Long> {

    private RoleRepository roleRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role getOne(Long id) {
        return null;
    }

    @Override
    public Role add(Role role) {
        return null;
    }

    @Override
    public Role update(Role role) {
        return null;
    }
}
