package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import pr4.t1.kempenrust.DTO.BoekingDetailDto;
import pr4.t1.kempenrust.model.Boeking;
import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.Klant;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;


@Repository
public class BoekingDetailRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ArrayList<BoekingDetail> getAlleToekomstigeBoekingdetails() {

        ArrayList<BoekingDetail> details = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                    "SELECT * FROM boekingDetails INNER JOIN " +
                        "(boekingen INNER JOIN klanten ON boekingen.klantId = klanten.klantId) " +
                        "ON boekingDetails.boekingId = boekingen.boekingID " +
                        "INNER JOIN kamers ON boekingDetails.kamerId = kamers.kamerId " +
                        "WHERE boekingen.datumVan > ? " +
                        "ORDER BY boekingen.datumVan",
                         Date.valueOf(LocalDate.now()));

        return opvullenBoekingsdetails(details, rowSet);
    }

    public ArrayList<BoekingDetail> getAlleBoekingsdetailsByMaand(int maand, int jaar) {
        ArrayList<BoekingDetail> details = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM boekingDetails INNER JOIN " +
                        "(boekingen INNER JOIN klanten ON boekingen.klantId = klanten.klantId) " +
                            "ON boekingDetails.boekingId = boekingen.boekingID " +
                        "INNER JOIN kamers ON boekingDetails.kamerId = kamers.kamerId " +
                        "WHERE (MONTH(boekingen.datumVan) = ? AND YEAR(boekingen.datumVan) = ?) " +
                            "OR (MONTH(boekingen.datumTot) = ? AND YEAR(boekingen.datumTot) = ?) " +
                        "ORDER BY boekingen.datumVan, boekingen.datumTot",
                maand, jaar,
                maand, jaar);

        return opvullenBoekingsdetails(details, rowSet);
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

        while (rowSet.next()) {
            Klant klant = new Klant();
            Boeking boeking = new Boeking();
            boeking.setAantalPersonen(rowSet.getInt("aantalPersonen"));
            boeking.setVerblijfsKeuzeID(rowSet.getInt("verblijfsKeuzeID"));
            boeking.setBoekingID(rowSet.getInt("boekingID"));

            boeking.setKlant(klant);
            reservering.setBoeking(boeking);

        }

        return reservering;
    }

    public int toevoegenBoekingsdetails(int boekingID, int kamerID) {
        String SqlInsertStatement = "" +
                "INSERT INTO BoekingDetails (BoekingID, KamerID) " +
                "VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SqlInsertStatement, new String[]{"BoekingDetailsID"});
            ps.setInt(1, boekingID);
            ps.setInt(2, kamerID);
            return ps;
        }, keyHolder);

        // returns the PK of the newly created record
        return keyHolder.getKey().intValue();
    }

    public ArrayList<BoekingDetail> getAfgelopenReservaties() {

        ArrayList<BoekingDetail> details = new ArrayList<>();

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
    public ArrayList<BoekingDetailDto> getAlleDetailsMetDatums(Date datumVan,Date datumTot ){
        ArrayList<BoekingDetailDto> details = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM " +
                        " BOEKINGDETAILS INNER JOIN BOEKINGEN " +
                            "ON BOEKINGDETAILS.BOEKINGID =BOEKINGEN.BOEKINGID " +
                        "INNER JOIN KAMERS " +
                            "ON BOEKINGDETAILS.KAMERID =KAMERS .KAMERID  " +
                        "INNER JOIN KLANTEN " +
                            "ON BOEKINGEN.KLANTID =KLANTEN .KLANTID " +
                        "WHERE BOEKINGEN.DatumTot < ? " +
                        "AND BOEKINGEN.DatumTot  BETWEEN '"+datumVan+"' AND '"+datumTot+"'" +
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

    private ArrayList<BoekingDetail> opvullenBoekingsdetails(ArrayList<BoekingDetail> details, SqlRowSet rowSet) {
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

            kamer.setKamerID(rowSet.getInt("KamerID"));
            kamer.setKamerNummer(rowSet.getInt("kamerNummer"));

            boeking.setKlant(klant);
            detail.setBoekingID(rowSet.getInt("boekingID"));
            detail.setKamer(kamer);
            detail.setBoeking(boeking);

            details.add(detail);
        }

        return details;
    }
}
