package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.model.Boeking;
import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.Klant;

@Repository
public class KlantRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Klant getKlantVoorBoeking(int boekingID) {
        Klant klant = new Klant();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM  " +
                        "boekingen INNER JOIN klanten ON boekingen.klantId = klanten.klantId " +
                        "WHERE " +
                        "boekingen.boekingId = ?"
                , boekingID);

        while(rowSet.next()) {
            klant.setNaam(rowSet.getString("naam"));
            klant.setVoornaam(rowSet.getString("voornaam"));
        }

        return klant;
    }
}
