package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import pr4.t1.kempenrust.DTO.BoekingDetailDto;
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

        Date datumVan = new Date(new java.util.Date().getTime());

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
            detail.setKamer(kamer);
            detail.setBoeking(boeking);

            details.add(detail);
        }

        return details;
    }

    public ArrayList<BoekingDetail> getAllPastDetails() {

        ArrayList<BoekingDetail> details = new ArrayList<>();

        Date datumVan = new Date(new java.util.Date().getTime());

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM boekingDetails INNER JOIN " +
                        "(boekingen INNER JOIN klanten ON boekingen.klantId = klanten.klantId) " +
                        "ON boekingDetails.boekingId = boekingen.boekingID " +
                        "INNER JOIN kamers ON boekingDetails.kamerId = kamers.kamerId " +
                        "WHERE boekingen.DatumTot < ? " +
                        "ORDER BY boekingen.DatumTot",
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
            detail.setKamer(kamer);
            detail.setBoeking(boeking);

            details.add(detail);
        }

        return details;
    }
    public ArrayList<BoekingDetailDto> getAllDetailsWithDates(Date datumVan,Date datumTot ){
        ArrayList<BoekingDetailDto> details = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM " +
                        "((" +
                        " (BOEKINGDETAILS INNER JOIN BOEKINGEN ON BOEKINGDETAILS.BOEKINGID =BOEKINGEN.BOEKINGID )" +
                        "INNER JOIN KAMERS ON BOEKINGDETAILS.KAMERID =KAMERS .KAMERID  )" +
                        "INNER JOIN KLANTEN ON BOEKINGEN.KLANTID =KLANTEN .KLANTID " +
                        ")" +
                        "WHERE BOEKINGEN.DatumTot < ? AND BOEKINGEN.DatumTot  BETWEEN '"+datumVan+"' AND '"+datumTot+"'" +
                        "ORDER BY BOEKINGEN.DatumTot", Date.valueOf(LocalDate.now()));

        while(rowSet.next()) {
            BoekingDetailDto detail = new BoekingDetailDto();
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
            detail.setKamer(kamer);
            detail.setBoeking(boeking);

            details.add(detail);
        }

        return details;
    }
}
