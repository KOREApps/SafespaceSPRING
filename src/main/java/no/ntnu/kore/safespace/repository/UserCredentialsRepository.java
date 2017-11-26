package no.ntnu.kore.safespace.repository;

import no.ntnu.kore.safespace.domain.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Class that handles retrieving user authentication information from the database.
 * @author robert
 */
@Repository
public class UserCredentialsRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public UserCredentials getUserCredentials(String username) {
        String sql = "SELECT u.username, u.password, r.name " +
                "FROM user_account u INNER JOIN role r on u.role = r.id " +
                "WHERE u.enabled = TRUE and u.username = ?";

        UserCredentials userInfo =
                (UserCredentials) jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> {
                    UserCredentials userInfo1 = new UserCredentials();
                    userInfo1.setUsername(rs.getString("username"));
                    userInfo1.setPassword(rs.getString("password"));
                    userInfo1.setRole(rs.getString("name"));
                    return userInfo1;
                });
        return userInfo;
    }

}
