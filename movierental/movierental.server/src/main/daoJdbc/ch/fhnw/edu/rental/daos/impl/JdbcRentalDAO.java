package ch.fhnw.edu.rental.daos.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ch.fhnw.edu.rental.daos.MovieDAO;
import ch.fhnw.edu.rental.daos.RentalDAO;
import ch.fhnw.edu.rental.daos.UserDAO;
import ch.fhnw.edu.rental.model.Movie;
import ch.fhnw.edu.rental.model.Rental;
import ch.fhnw.edu.rental.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class JdbcRentalDAO implements RentalDAO {

    private DataSource ds;
    private MovieDAO movieDAO;
    private UserDAO userDAO;

    public void setDataSource(DataSource dataSource) {
        this.ds = dataSource;
    }

    public void setMovieDAO(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Rental getById(Long id) {
        JdbcTemplate template = new JdbcTemplate(ds);
        Map<String, Object> result = template.queryForMap("select * from RENTALS where RENTAL_ID = ", id);
        JdbcUserDAO userDAO = new JdbcUserDAO();
        JdbcMovieDAO movieDAO = new JdbcMovieDAO();
        userDAO.setDataSource(ds);
        movieDAO.setDataSource(ds);

        return new Rental(
                userDAO.getById((Long) result.get("USER_ID")),
                movieDAO.getById((Long) result.get("MOVIE_ID")),
                (Integer) result.get("RENTAL_RENTALDAYS")
        );

    }

    @Override
    public List<Rental> getAll() {
        JdbcTemplate template = new JdbcTemplate();
        return template.query(
                "select * from MOVIES ",
                new RowMapper<Rental>() {
                    public Rental mapRow(ResultSet rs, int row)
                            throws SQLException {
                        return new Rental(
                                userDAO.getById(rs.getLong("USER_ID")),
                                movieDAO.getById(rs.getLong("MOVIE_ID")),
                                rs.getInt("RENTAL_RENTALDAYS")
                        );
                    }
                }
        );
    }

    @Override
    public List<Rental> getRentalsByUser(User user) {
        JdbcTemplate template = new JdbcTemplate();
        return template.query(
             "select * from RENTALS where USER_ID = ?", new RowMapper<Rental>() {
                public Rental mapRow(ResultSet rs, int row)
                        throws SQLException {
                    return new Rental(
                            userDAO.getById(rs.getLong("USER_ID")),
                            movieDAO.getById(rs.getLong("MOVIE_ID")),
                            rs.getInt("RENTAL_RENTALDAYS")
                    );
                }
            },
            user.getId()
        );

    }

    @Override
    public void saveOrUpdate(Rental rental) {
                JdbcTemplate template = new JdbcTemplate(ds);
        int noEntries = 0;
        noEntries = template.queryForInt("select * from RENTALS where RENTAL_ID = ?",rental.getId());
        if(noEntries == 0){
            template.update("INSERT INTO RENTALS (RENTAL_ID, RENTAL_RENTALDATE, RENTAL_RENTALDAYS, USER_ID, MOVIE_ID) VALUES (?,?,?,?,?)",
                    rental.getUser(),
                    rental.getRentalDate(),
                    rental.getRentalDays(),
                    rental.getUser().getId(),
                    rental.getMovie().getId());
        }else{
            template.update("UPDATE RENTALS SET RENTAL_RENTALDATE=?, RENTAL_RENTALDAYS=?, USER_ID=?, MOVIE_ID=? where RENTAL_ID=?",
                    rental.getRentalDate(),
                    rental.getRentalDays(),
                    rental.getUser().getId(),
                    rental.getMovie().getId()
            );
        }
    }

    @Override
    public void delete(Rental rental) {
        JdbcTemplate template = new JdbcTemplate(ds);
        template.execute("delete from RENTALS where RENTAL_ID = " + rental.getId());

    }

    private Rental createRental(ResultSet rs) throws SQLException {
        User user = userDAO.getById(rs.getLong("USER_ID"));
        return createRental(rs, user);
    }

    private Rental createRental(ResultSet rs, User u) throws SQLException {
        Movie m = movieDAO.getById(rs.getLong("MOVIE_ID"));
        if (!m.isRented())
            throw new RuntimeException("movie must be rented if read from DB");
        m.setRented(false);
        Rental r = new Rental(u, m, rs.getInt("RENTAL_RENTALDAYS"));
        m.setRented(true);
        r.setId(rs.getLong("RENTAL_ID"));
        return r;
    }

}
