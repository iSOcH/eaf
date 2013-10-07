package ch.fhnw.edu.rental.daos.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ch.fhnw.edu.rental.daos.RentalDAO;
import ch.fhnw.edu.rental.daos.UserDAO;
import ch.fhnw.edu.rental.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class JdbcUserDAO implements UserDAO {

    private DataSource ds;
    private RentalDAO rentalDAO;

    public void setDataSource(DataSource dataSource) {
        this.ds = dataSource;
    }

    public void setRentalDAO(RentalDAO rentalDAO) {
        this.rentalDAO = rentalDAO;
    }

    @Override
    public User getById(Long id) {
        JdbcTemplate template = new JdbcTemplate(ds);

        Map<String, Object> result = template.queryForMap("SELECT * FROM Users WHERE USER_ID = ?", id);
        User user = new User(
                (String) result.get("USER_FIRSTNAME"),
                (String) result.get("USER_NAME")
        );
        user.setEmail((String) result.get("USER_EMAIL"));
        user.setId(id);
        user.setRentals(rentalDAO.getRentalsByUser(user));
        //TODO: roles
        return user;
    }

    @Override
    public List<User> getAll() {
        JdbcTemplate template = new JdbcTemplate(ds);
        return template.query(
                "SELECT * from USERS",
                new RowMapper<User>() {
                    public User mapRow(ResultSet rs, int row)
                            throws SQLException {
                        User user = new User(
                                rs.getString("USER_NAME"),
                                rs.getString("USER_FIRSTNAME"));
                        user.setEmail(rs.getString("USER_EMAIL"));
                        user.setId(rs.getLong("USER_ID"));
                        user.setRentals(rentalDAO.getRentalsByUser(user));
                        return user;

                    }
                }
        );
    }


    @Override
    public List<User> getByName(String name) {
        JdbcTemplate template = new JdbcTemplate(ds);

        return template.query(
                "SELECT * FROM USERS WHERE USER_NAME = ?",
                new RowMapper<User>() {
                    public User mapRow(ResultSet rs, int row)
                            throws SQLException {
                        User user = new User(
                                rs.getString("USER_NAME"),
                                rs.getString("USER_FIRSTNAME"));
                        user.setEmail(rs.getString("USER_EMAIL"));
                        user.setId(rs.getLong("USER_ID"));
                        user.setRentals(rentalDAO.getRentalsByUser(user));
                        return user;

                    }
                },
                name
        );

    }


    @Override
    public void saveOrUpdate(User user) {

        JdbcTemplate template = new JdbcTemplate(ds);
        int noEntries = 0;
        noEntries = template.queryForInt("select count(*) from USERS where USER_ID = ?", user.getId());
        if (noEntries == 0) {
            template.update("INSERT INTO USERS (USER_ID, USER_VERSION, USER_EMAIL, USER_NAME, USER_FIRSTNAME) VALUES ?,?,?,?",
                    user.getId(),
                    0,
                    user.getEmail(),
                    user.getLastName(),
                    user.getFirstName()
            );
        } else {
            template.update("UPDATE USERS SET USER_ID=?, USER_VERSION=?, USER_EMAIL=?, USER_NAME=?, USER_FIRSTNAME=? where USER_ID=?",
                    user.getId(),
                    0,
                    user.getEmail(),
                    user.getLastName(),
                    user.getFirstName(),
                    user.getId());
        }
    }


    @Override
    public void delete(User user) {
        JdbcTemplate template = new JdbcTemplate(ds);
        template.execute("delete from USERS where USER_ID = " + user.getId());
    }
}
