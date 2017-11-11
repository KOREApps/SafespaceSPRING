package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.domain.UserCredentials;
import no.ntnu.kore.safespace.entity.Role;
import no.ntnu.kore.safespace.entity.User;
import no.ntnu.kore.safespace.repository.RoleRepository;
import no.ntnu.kore.safespace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController implements RestService<User, Long> {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getOne(Long id) {
        return new ResponseEntity<>(userRepository.findOne(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity add(@RequestBody User user) {
        if (user.getRole() == null) {
            user.setRole(roleRepository.getRoleByNameIgnoreCase("USER"));
        }
        ValidCheckResult validCheckResult = validPost(user);
        if (validCheckResult.isValid()) {
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validCheckResult, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPost(User newEntity) {
        if (newEntity.getId() != null) {
            return new ValidCheckResult(false, "New entity id must be null");
        } else if (userRepository.findUserByUsernameIgnoreCase(newEntity.getUsername()) != null) {
            return new ValidCheckResult(false, "Username is already taken");
        }
        return ValidCheckResult.OK;
    }

    @Override
    public ResponseEntity<User> update(Long id, @RequestBody User user) {
        ValidCheckResult validCheckResult = validPut(user, id);
        if (validCheckResult.isValid()) {
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ValidCheckResult validPut(User newEntity, Long id) {
        User currentUser = userRepository.findOne(id);
        if (currentUser == null) {
            return new ValidCheckResult(false, "Id does not exist");
        } else if(newEntity.getId() == null || !newEntity.getId().equals(id)) {
            return new ValidCheckResult(false, "Id in json does not match id in path");
        }
        return ValidCheckResult.OK;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity getUserWithCredentials(@RequestBody UserCredentials userCredentials) {
        User user = userRepository.findUserByUsernameIgnoreCase(userCredentials.getUsername());
        if (user != null && userCredentials.getPassword().equalsIgnoreCase(user.getPassword())) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
