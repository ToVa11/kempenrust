package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.model.Boeking;
import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.Klant;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public class BoekingDetailRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ArrayList<BoekingDetail> getAllFutureDetails() {

        ArrayList<BoekingDetail> details = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                    "SELECT * FROM boekingDetails INNER JOIN " +
                        "(boekingen INNER JOIN klanten ON boekingen.klantId = klanten.klantId) " +
                        "ON boekingDetails.boekingId = boekingen.boekingID " +
                        "INNER JOIN kamers ON boekingDetails.kamerId = kamers.kamerId " +
                        "WHERE boekingen.datumVan > ? " +
                        "ORDER BY boekingen.datumVan",
                         Date.valueOf(LocalDate.now()));
        
        while(rowSet.next()) {
            BoekingDetail detail = new BoekingDetail();
            Klant klant = new Klant();
            Boeking boeking = new Boeking();
            Kamer kamer = new Kamer();

            klant.setEmail(rowSet.getString("email"));
            klant.setNaam(rowSet.getString("naam"));
            klant.setVoornaam(rowSet.getString("voornaam"));

            boeking.setDatumVan(rowSet.getDate("datumVan"));
            boeking.setDatumTot(rowSet.getDate("datumTot"));

            kamer.setKamerNummer(rowSet.getInt("kamerNummer"));

            boeking.setKlant(klant);
            detail.setBoekingID(rowSet.getInt("boekingID"));
            detail.setKamer(kamer);
            detail.setBoeking(boeking);

            details.add(detail);
        }

        return details;
    }

    public BoekingDetail getReserveringByID(int ID) {
        BoekingDetail reservering = new BoekingDetail();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM boekingdetails INNER JOIN " +
                        "(boekingen INNER JOIN klanten ON boekingen.klantId = klanten.klantId) " +
                        "ON boekingDetails.boekingId = boekingen.boekingID " +
                        "WHERE " +
                        "boekingdetails.boekingId = ?"
                , ID);

        while(rowSet.next()) {
            Klant klant = new Klant();
            Boeking boeking = new Boeking();

            klant.setEmail(rowSet.getString("email"));
            klant.setNaam(rowSet.getString("naam"));
            klant.setVoornaam(rowSet.getString("voornaam"));

            boeking.setDatumVan(rowSet.getDate("datumVan"));
            boeking.setDatumTot(rowSet.getDate("datumTot"));
            boeking.setAantalPersonen(rowSet.getInt("aantalPersonen"));
            boeking.setVerblijfsKeuzeID(rowSet.getInt("verblijfsKeuzeID"));
            boeking.setBoekingID(rowSet.getInt("boekingID"));

            boeking.setKlant(klant);
            reservering.setBoeking(boeking);

        }

        return reservering;
    }
}
