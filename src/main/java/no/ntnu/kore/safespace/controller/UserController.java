package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.domain.UserCredentials;
import no.ntnu.kore.safespace.entity.Role;
import no.ntnu.kore.safespace.entity.User;
import no.ntnu.kore.safespace.repository.RoleRepository;
import no.ntnu.kore.safespace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest controller for users. Currently handles Create, Read and Update operations on users.
 * This controller can also check if submitted user credentials are valid.
 * @author robert
 */
@RestController
@RequestMapping("users")
public class UserController implements RestService<User, Long> {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
            hashUserPassword(user);
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validCheckResult, HttpStatus.BAD_REQUEST);
        }
    }

    private void hashUserPassword(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Override
    public ValidCheckResult validPost(User newEntity) {
        if (newEntity.getId() != null) {
            return new ValidCheckResult(false, "New entity id must be null");
        } else if (userRepository.findUserByUsernameIgnoreCase(newEntity.getUsername()) != null) {
            return new ValidCheckResult(false, "Username is already taken");
        } else if (newEntity.getPassword() == null) {
            return new ValidCheckResult(false, "Password can't be null value");
        } else if (newEntity.getPassword().length() <= 8) {
            return new ValidCheckResult(false, "Password must be 8 or more characters");
        } else if (newEntity.getRole() == null || !newEntity.getRole().getName().equals("ROLE_USER")) {
            setDefaultRole(newEntity);
        }
        return ValidCheckResult.OK;
    }

    private void setDefaultRole(User newUser) {
        Role defaultRole = roleRepository.getRoleByNameIgnoreCase("ROLE_USER");
        newUser.setRole(defaultRole);
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

    /**
     * Checks if the given user credentials are valid.
     * @param userCredentials the user credentials.
     * @return ResponseEntity containing the user object and code 200 OK if the credentials were valid. If credentials
     * were invalid the response is empty with code 400 BAD_REQUEST
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity getUserWithCredentials(@RequestBody UserCredentials userCredentials) {
        User user = userRepository.findUserByUsernameIgnoreCase(userCredentials.getUsername());
        if (user != null && passwordEncoder.matches(userCredentials.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
