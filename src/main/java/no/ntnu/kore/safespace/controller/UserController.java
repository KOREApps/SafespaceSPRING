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
        return new ResponseEntity<List<User>>(userRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getOne(Long id) {
        return new ResponseEntity<User>(userRepository.findOne(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> add(@RequestBody User user) {
        return new ResponseEntity<User>(userRepository.save(user), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> update(Long id, User user) {
        if (userRepository.exists(id)) {
            return new ResponseEntity<User>(userRepository.save(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }
}
