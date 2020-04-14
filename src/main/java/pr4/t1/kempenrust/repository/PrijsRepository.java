package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.DTO.KamerBeheer;

@Repository
public class PrijsRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public void prijsVerwijderen(int kamerID){
        jdbcTemplate.update("DELETE FROM Prijzen WHERE KamerID =? ",kamerID);
    }
    public KamerBeheer kamerTeVerwijderen(int kamerID){
        KamerBeheer kamer=new KamerBeheer();
        SqlRowSet rowSet= jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM " +
                "kamers  INNER JOIN Prijzen " +
                    "ON kamers.KAMERID = Prijzen.KAMERID " +
                "WHERE Kamers.kamerID = ? ",kamerID);
        while(rowSet.next()){
            kamer.setKamerID(rowSet.getInt("KamerID"));
            prijsVerwijderen(kamerID);
        }
        return  kamer;
    }

}
