package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.DTO.KamerBeheer;

@Repository
public class BoekingRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public KamerBeheer GeboekteKamer(int kamerID){
        KamerBeheer kamer=new KamerBeheer();
        SqlRowSet rowSet= jdbcTemplate.queryForRowSet("SELECT *" +
                "from " +
                "(" +
                "(" +
                "     kamers  INNER JOIN BoekingDetails " +
                "    ON kamers.KAMERID = BoekingDetails.KamerID " +
                ")" +
                "INNER JOIN Boekingen " +
                "ON Boekingen.BoekingID = BoekingDetails.BoekingID" +
                ")" +
                "WHERE Kamers.kamerID = ? ",kamerID);
        while(rowSet.next()){
            kamer.setKamerID(rowSet.getInt("KamerID"));
            kamer.setKamerNummer(rowSet.getInt("KamerNummer"));
            kamer.setDatumVan(rowSet.getDate("DatumVan"));
            kamer.setDatumTot(rowSet.getDate("DatumTot"));
            kamer.setGeboekt(true);
        }
        return  kamer;
    }

}
