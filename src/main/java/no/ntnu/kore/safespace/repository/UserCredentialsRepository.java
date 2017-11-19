package no.ntnu.kore.safespace.repository;

import no.ntnu.kore.safespace.domain.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                (UserCredentials) jdbcTemplate.queryForObject(sql, new Object[]{username}, new RowMapper<UserCredentials>() {
                    @Override
                    public UserCredentials mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UserCredentials userInfo = new UserCredentials();
                        userInfo.setUsername(rs.getString("username"));
                        userInfo.setPassword(rs.getString("password"));
                        userInfo.setRole(rs.getString("name"));
                        return userInfo;
                    }
                });
        return userInfo;
    }

}