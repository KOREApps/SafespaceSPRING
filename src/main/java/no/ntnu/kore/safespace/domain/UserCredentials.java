package no.ntnu.kore.safespace.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ntnu.kore.safespace.entity.Role;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCredentials {

    private String username;
    private String password;
    private String role;

}
