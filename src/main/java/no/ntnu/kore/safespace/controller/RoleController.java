package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.Role;
import no.ntnu.kore.safespace.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("roles")
public class RoleController implements RestService<Role, Long> {

    private RoleRepository roleRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity getAll() {
        return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getOne(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(roleRepository.findOne(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity add(@RequestBody Role role) {
        ValidCheckResult validCheckResult = validPost(role);
        if (validCheckResult.isValid()) {
            role.setId(null);
            role = roleRepository.save(role);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validCheckResult, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPost(Role newEntity) {
        if (newEntity.getId() != null) {
            return new ValidCheckResult(false, "New entity id must be null");
        }
        return ValidCheckResult.OK;
    }

    @Override
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody Role role) {
        ValidCheckResult validCheckResult = validPut(role, id);
        if (validCheckResult.isValid()) {
            role = roleRepository.save(role);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validCheckResult, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPut(Role newEntity, Long id) {
        Role currentRole = roleRepository.findOne(id);
        if (currentRole == null) {
            return new ValidCheckResult(false, "Id does not exist");
        } else if(newEntity.getId() == null || !newEntity.getId().equals(id)) {
            return new ValidCheckResult(false, "Id in json does not match id in path");
        }
        return ValidCheckResult.OK;
    }
}
