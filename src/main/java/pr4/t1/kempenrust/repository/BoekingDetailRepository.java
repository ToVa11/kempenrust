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
import java.util.List;


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

    public List<BoekingDetail> getDetailsVoorBoeking(int boekingID) {
        List<BoekingDetail> boekingDetails = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM boekingdetails INNER JOIN " +
                        "boekingen ON boekingDetails.boekingId = boekingen.boekingID " +
                        "INNER JOIN kamers ON boekingDetails.kamerID = kamers.kamerID " +
                        "WHERE " +
                        "boekingdetails.boekingId = ?"
                , boekingID);

        while (rowSet.next()) {
            BoekingDetail details = new BoekingDetail();
            Boeking boeking = new Boeking();

            boeking.setAantalPersonen(rowSet.getInt("aantalPersonen"));
            boeking.setVerblijfsKeuzeID(rowSet.getInt("verblijfsKeuzeID"));
            boeking.setBoekingID(rowSet.getInt("boekingID"));
            boeking.setDatumVan(rowSet.getDate("datumVan"));
            boeking.setDatumTot(rowSet.getDate("datumTot"));

            details.setKamerID(rowSet.getInt("kamerID"));
            details.setBoeking(boeking);

            boekingDetails.add(details);
        }

        return boekingDetails;
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

        ArrayList<BoekingDetail> reservaties = new ArrayList<>();

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

            reservaties.add(detail);
        }

        return reservaties;
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


    public List<BoekingDetail> getBoekingenZonderHuidigeBoeking(int boekingId, Date datumVan, Date datumTot){
        List<BoekingDetail> detailsBoekingen = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("" +
                "SELECT * FROM Boekingen " +
                "INNER JOIN BoekingDetails ON Boekingen.BoekingID = BoekingDetails.BoekingID " +
                "INNER JOIN Kamers ON BoekingDetails.KamerID = Kamers.KamerID " +
                "WHERE (Boekingen.DatumVan BETWEEN ? AND ? " +
                "OR Boekingen.DatumTot BETWEEN ? AND ? )" +
                "AND Boekingen.BoekingID != ? ",
                datumVan,
                datumTot,
                datumVan,
                datumTot,
                boekingId
        );

        while(rowSet.next()) {
            Boeking boeking = new Boeking();
            BoekingDetail details = new BoekingDetail();
            Kamer kamer = new Kamer();

            boeking.setBoekingID(rowSet.getInt("boekingID"));
            boeking.setVerblijfsKeuzeID(rowSet.getInt("verblijfskeuzeID"));
            boeking.setDatumTot(rowSet.getDate("datumTot"));
            boeking.setDatumVan(rowSet.getDate("datumVan"));

            kamer.setKamerID(rowSet.getInt("kamerID"));
            kamer.setKamerNummer(rowSet.getInt("kamernummer"));

            details.setBoekingDetailID(rowSet.getInt("boekingDetailsID"));
            details.setKamer(kamer);
            details.setBoeking(boeking);

            detailsBoekingen.add(details);
        }

        return detailsBoekingen;
    }
}
