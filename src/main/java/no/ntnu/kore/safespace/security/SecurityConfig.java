package no.ntnu.kore.safespace.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    Environment environment;
    @Autowired
    AuthenticationService authenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .hasRole("ADMIN")
                .and()
                .httpBasic();
        http.authorizeRequests().and().antMatcher("/users").authorizeRequests().anyRequest().permitAll();
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("ss")
                .password("safespace")
                .roles("ADMIN");
        auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder);
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
//                .authoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?")
//                .passwordEncoder(passwordEncoder);

//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .withUser("robert").roles("ADMIN").password(passwordEncoder.encode("asdf1234"))
//                .and()
//                .withUser("rabbert").roles("USER").password(passwordEncoder.encode("ASDF1234"));
    }


}
