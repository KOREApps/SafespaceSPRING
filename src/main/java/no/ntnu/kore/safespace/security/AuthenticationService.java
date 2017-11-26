package no.ntnu.kore.safespace.security;

import no.ntnu.kore.safespace.domain.UserCredentials;
import no.ntnu.kore.safespace.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Class that provide spring security with user authentication information.
 * @author robert
 */
@Service
public class AuthenticationService implements UserDetailsService {

    private UserCredentialsRepository credentialsRepository;

    @Autowired
    public AuthenticationService(UserCredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentials credentials = credentialsRepository.getUserCredentials(username);
        GrantedAuthority authority = new SimpleGrantedAuthority(credentials.getRole());
        return new User(credentials.getUsername(), credentials.getPassword(), Arrays.asList(authority));
    }
}
