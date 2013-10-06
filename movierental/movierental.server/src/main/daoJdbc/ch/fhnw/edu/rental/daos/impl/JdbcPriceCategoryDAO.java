package ch.fhnw.edu.rental.daos.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ch.fhnw.edu.rental.daos.PriceCategoryDAO;
import ch.fhnw.edu.rental.model.PriceCategory;
import ch.fhnw.edu.rental.model.PriceCategoryChildren;
import ch.fhnw.edu.rental.model.PriceCategoryNewRelease;
import ch.fhnw.edu.rental.model.PriceCategoryRegular;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class JdbcPriceCategoryDAO implements PriceCategoryDAO {

    private DataSource ds;

    public void setDataSource(DataSource dataSource) {
        this.ds = dataSource;
    }

    @Override
    public PriceCategory getById(Long id) {

        JdbcTemplate template = new JdbcTemplate(ds);
        Map<String, Object> res = template.queryForMap(
                "select * from PRICECATEGORIES where PRICECATEGORY_ID = ?", id);
        Integer pc_id = (Integer) res.get("PRICECATEGORY_ID");
        String pc_type = (String) res.get("PRICECATEGORY_TYPE");
        switch (pc_id) {
            case 1:
                return new PriceCategoryRegular();
            case 2:
                return new PriceCategoryChildren();
            case 3:
                return new PriceCategoryNewRelease();
            default:
                throw new IllegalArgumentException("no suitable price category found for ID " + pc_id + "\n");
        }
    }

    @Override
    public List<PriceCategory> getAll() {
        JdbcTemplate template = new JdbcTemplate(ds);
        return template.query("SELECT * FROM PRICECATEGORIES",
                new RowMapper<PriceCategory>() {
                    @Override
                    public PriceCategory mapRow(ResultSet resultSet, int i) throws SQLException {
                        int pc_id = resultSet.getInt("PRICECATEGORY_ID");

                        switch (pc_id) {
                            case 1:
                                return new PriceCategoryRegular();
                            case 2:
                                return new PriceCategoryChildren();
                            case 3:
                                return new PriceCategoryNewRelease();
                            default:
                                throw new IllegalArgumentException("no suitable price category found for ID " + pc_id + "\n");
                        }
                    }
                });
    }

    @Override
    public void saveOrUpdate(PriceCategory priceCategory) {
        JdbcTemplate template = new JdbcTemplate(ds);
        int numRows = 0;
        numRows = template.queryForInt("SELECT COUNT(*) FROM PRICECATEGORIES WHERE PRICECATEGORY_ID =?",priceCategory.getId());
        if(numRows == 0){
            template.update("INSERT INTO PRICECATEGORIES (PRICECATEGORY_ID, PRICECATEGORY_TYPE) VALUES ?,?",priceCategory.getId(),priceCategory.toString()+"PriceCategory");
        }else{
            template.update("UPDATE PRICECATEGORIES SET PRICECATEGORY_ID = ?, PRICECATEGORY_TYPE = ?",priceCategory.getId(),priceCategory.toString()+"PriceCategory");
        }
    }

    @Override
    public void delete(PriceCategory priceCategory) {
         JdbcTemplate template = new JdbcTemplate(ds);
         template.queryForInt("DELETE FROM PRICECATEGORIES WHERE PRICECATEGORY_ID = ?",priceCategory.getId());
    }
}
