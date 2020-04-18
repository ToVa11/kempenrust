package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.model.Boeking;

import java.sql.Date;
import java.sql.Types;

@Repository
public class BoekingRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int updateBoeking(String datumVan, String datumTot, int aantalPersonen, int verblijfskeuzeID, int boekingID){
        Object[] params = {Date.valueOf(datumVan),Date.valueOf(datumTot),aantalPersonen,verblijfskeuzeID,boekingID};
        int[] types = {Types.DATE,Types.DATE,Types.INTEGER,Types.INTEGER,Types.INTEGER};

        String sql = "UPDATE boekingen SET datumVan=?,datumTot=?,aantalPersonen=?,verblijfskeuzeID=? WHERE boekingID=?";

        int rows = jdbcTemplate.update(sql,params,types);

        return rows;
    }
}
