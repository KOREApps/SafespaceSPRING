package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.Role;
import no.ntnu.kore.safespace.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<List<Role>> getAll() {
        return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Role> getOne(Long id) {
        return new ResponseEntity<>(roleRepository.findOne(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Role> add(@RequestBody Role role) {
        role.setId(null);
        role = roleRepository.save(role);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Role> update(Long id, @RequestBody Role role) {
        if (roleRepository.exists(id)) {
            role = roleRepository.save(role);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
