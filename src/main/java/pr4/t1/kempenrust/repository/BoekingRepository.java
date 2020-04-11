package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;

@Repository
public class BoekingRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int createReservation(int klantID, int verblijfsKeuzeID, BigDecimal bedragVoorschot, int aantalPersonen, Date datumVan, Date datumTot) {
        String SqlInsertStatement = "" +
                "INSERT INTO Boekingen (KlantID, VerblijfsKeuzeID, Datum, BedragVoorschot, AantalPersonen, DatumVan, DatumTot, IsBetaald) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SqlInsertStatement, new String[] {"BoekingID"});
            ps.setInt(1, klantID);
            ps.setInt(2, verblijfsKeuzeID);
            ps.setDate(3, new Date(new java.util.Date().getTime()));
            ps.setBigDecimal(4, bedragVoorschot);
            ps.setInt(5, aantalPersonen);
            ps.setDate(6, datumVan);
            ps.setDate(7, datumTot);
            ps.setBoolean(8, false);
            return ps;
        }, keyHolder);

        // returns the PK of the newly created record
        return keyHolder.getKey().intValue();
    }
}
