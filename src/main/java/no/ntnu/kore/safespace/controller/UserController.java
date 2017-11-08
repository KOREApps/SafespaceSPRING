package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.User;
import no.ntnu.kore.safespace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController implements RestService<User, Long> {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
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
    public ResponseEntity<User> add(@RequestBody User user) {
        ValidCheckResult validCheckResult = validPost(user);
        if (validCheckResult.isValid()) {
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPost(User newEntity) {
        if (newEntity.getId() != null) {
            return new ValidCheckResult(false, "New entity id must be null");
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
}
