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
import ch.fhnw.edu.rental.daos.PriceCategoryDAO;
import ch.fhnw.edu.rental.model.Movie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class JdbcMovieDAO implements MovieDAO {

    private DataSource ds;
    private PriceCategoryDAO priceCategoryDAO;

    public void setDataSource(DataSource dataSource) {
        this.ds = dataSource;
    }

    public void setPriceCategoryDAO(PriceCategoryDAO priceCategoryDAO) {
        this.priceCategoryDAO = priceCategoryDAO;
    }

    @Override
    public Movie getById(Long id) {
        JdbcTemplate template = new JdbcTemplate(ds);
        Map<String, Object> res = template.queryForMap(
                "select * from MOVIES where MOVIE_ID = ?", id);
        long priceCategory = (Long) res.get("PRICECATEGORY_FK");
        Movie m = new Movie(
                (Long) res.get("MOVIE_ID"),
                (String) res.get("MOVIE_TITLE"),
                (java.sql.Timestamp) res.get("MOVIE_RELEASEDATE"),
                (Boolean) res.get("MOVIE_RENTED"),
                priceCategoryDAO.getById(priceCategory));
        return m;
    }
@Override
    public List<Movie> getByTitle(String name) {
        JdbcTemplate template = new JdbcTemplate();
        return template.query(
                "select * from MOVIES where MOVIE_TITLE = ?",
                new RowMapper<Movie>() {
                    public Movie mapRow(ResultSet rs, int row)
                            throws SQLException {
                        long priceCategory = rs.getLong("PRICECATEGORY_FK");
                        return new Movie(
                                rs.getLong("MOVIE_ID"),
                                rs.getString("MOVIE_TITLE"),
                                rs.getTimestamp("MOVIE_RELEASEDATE"),
                                rs.getBoolean("MOVIE_RENTED"),
                                priceCategoryDAO.getById(priceCategory));
                    }
                },
                name
        );
}
    @Override
    public List<Movie> getAll() {
                JdbcTemplate template = new JdbcTemplate();
        return template.query(
                "select * from MOVIES ",
                new RowMapper<Movie>() {
                    public Movie mapRow(ResultSet rs, int row)
                            throws SQLException {
                        long priceCategory = rs.getLong("PRICECATEGORY_FK");
                        return new Movie(
                                rs.getLong("MOVIE_ID"),
                                rs.getString("MOVIE_TITLE"),
                                rs.getTimestamp("MOVIE_RELEASEDATE"),
                                rs.getBoolean("MOVIE_RENTED"),
                                priceCategoryDAO.getById(priceCategory));
                    }
                }
        );
    }

    @Override
    public void saveOrUpdate(Movie movie) {

        JdbcTemplate template = new JdbcTemplate(ds);
        int noEntries = 0;
        noEntries = template.queryForInt("select count(*) from MOVIES where MOVIE_ID = ?",movie.getId());
        if(noEntries == 0){
            template.update("INSERT INTO MOVIES (MOVIE_ID, MOVIE_TITLE, MOVIE_RELEASEDATE, MOVIE_RENTED, PRICECATEGORY_FK) VALUES (?,?,?,?,?)",
                    movie.getId(),
                    movie.getTitle(),
                    movie.getReleaseDate(),
                    movie.isRented(),
                    movie.getPriceCategory());
        }else{
            template.update("UPDATE MOVIES SET MOVIE_TITLE=?, MOVIE_RELEASEDATE=?, MOVIE_RENTED=?, PRICECATEGORY_FK=? where MOVIE_ID=?",
                    movie.getId());
        }
    }

    @Override
    public void delete(Movie movie) {
        JdbcTemplate template = new JdbcTemplate(ds);
        template.execute("delete from MOVIES where MOVIE_ID = " + movie.getId());
    }

    private Movie createMovie(ResultSet rs) throws SQLException {
        long priceCategory = rs.getLong("PRICECATEGORY_FK");
        Movie m = new Movie(
                rs.getString("MOVIE_TITLE"),
                rs.getDate("MOVIE_RELEASEDATE"),
                priceCategoryDAO.getById(priceCategory));
        m.setId(rs.getLong("MOVIE_ID"));
        m.setRented(rs.getBoolean("MOVIE_RENTED"));
        return m;
    }

}
