package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.model.Boeking;
import pr4.t1.kempenrust.model.Klant;

import java.sql.Date;
import java.sql.Types;
import pr4.t1.kempenrust.model.VerblijfsKeuze;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import pr4.t1.kempenrust.DTO.KamerBeheer;

@Repository
public class BoekingRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boeking getBoeking(int boekingID) {
        Boeking boeking = new Boeking();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM  " +
                        "boekingen INNER JOIN klanten ON boekingen.klantId = klanten.klantId " +
                        "WHERE " +
                        "boekingen.boekingId = ?"
                , boekingID);

        while(rowSet.next()) {
            Klant klant = new Klant();
            klant.setVoornaam(rowSet.getString("voornaam"));
            klant.setNaam(rowSet.getString("naam"));

            boeking.setDatumVan(rowSet.getDate("datumVan"));
            boeking.setDatumTot(rowSet.getDate("datumTot"));
            boeking.setAantalPersonen(rowSet.getInt("aantalPersonen"));
            boeking.setVerblijfsKeuzeID(rowSet.getInt("verblijfskeuzeID"));
            boeking.setBoekingID(boekingID);
            boeking.setKlant(klant);
        }
        return boeking;
    }

    @Autowired
    private BoekingDetailRepository boekingDetailRepository;

    public Boeking getReservatieByID(int boekingID) {
        Boeking boeking = null;

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("" +
                "SELECT  BoekingID, Boekingen.KlantID, Boekingen.VerblijfsKeuzeID, Datum, BedragVoorschot, AantalPersonen, DatumVan, DatumTot, IsBetaald, " +
                        "Voornaam, Klanten.Naam AS Familienaam, Straat, Huisnummer, Postcode, Gemeente, Telefoonnummer, Email, " +
                        "VerblijfsKeuzes.Naam AS VerblijfskeuzeNaam, Omschrijving " +
                "FROM Boekingen INNER JOIN Klanten " +
                    "ON Boekingen.KlantID = Klanten.KlantID " +
                "INNER JOIN VerblijfsKeuzes " +
                    "ON Boekingen.VerblijfsKeuzeID = VerblijfsKeuzes.VerblijfsKeuzeID " +
                "WHERE BoekingID = ?",
                boekingID);

        while(rowSet.next()) {
            boeking = new Boeking();
            Klant klant = new Klant();
            VerblijfsKeuze verblijfsKeuze = new VerblijfsKeuze();

            klant.setKlantID(rowSet.getInt("KlantID"));
            klant.setVoornaam(rowSet.getString("Voornaam"));
            klant.setNaam(rowSet.getString("FAMILIENAAM")); // Bij aliases moet je CAPITILIZED doorgeven
            klant.setStraat(rowSet.getString("Straat"));
            klant.setHuisnummer(rowSet.getString("Huisnummer"));
            klant.setPostcode(rowSet.getString("Postcode"));
            klant.setGemeente(rowSet.getString("Gemeente"));
            klant.setTelefoonnummer(rowSet.getString("Telefoonnummer"));
            klant.setEmail(rowSet.getString("Email"));

            verblijfsKeuze.setVerblijfskeuzeID(rowSet.getInt("VerblijfsKeuzeID"));
            verblijfsKeuze.setNaam(rowSet.getString("VERBLIJFSKEUZENAAM")); // Bij aliases moet je CAPITILIZED doorgeven
            verblijfsKeuze.setOmschrijving(rowSet.getString("Omschrijving"));

            boeking.setBoekingID(rowSet.getInt("BoekingID"));
            boeking.setKlantID(rowSet.getInt("KlantID"));
            boeking.setKlant(klant);
            boeking.setVerblijfsKeuzeID(rowSet.getInt("VerblijfsKeuzeID"));
            boeking.setVerblijfsKeuze(verblijfsKeuze);
            boeking.setDatum(rowSet.getDate("Datum"));
            boeking.setBedragVoorschot(rowSet.getBigDecimal("BedragVoorschot"));
            boeking.setAantalPersonen(rowSet.getInt("AantalPersonen"));
            boeking.setDatumVan(rowSet.getDate("DatumVan"));
            boeking.setDatumTot(rowSet.getDate("DatumTot"));
            boeking.setBetaald(rowSet.getBoolean("IsBetaald"));
        }

        return boeking;
    }

    public int updateBoeking(String datumVan, String datumTot, int aantalPersonen, int verblijfskeuzeID, int boekingID){
        Object[] params = {Date.valueOf(datumVan),Date.valueOf(datumTot),aantalPersonen,verblijfskeuzeID,boekingID};
        int[] types = {Types.DATE,Types.DATE,Types.INTEGER,Types.INTEGER,Types.INTEGER};

        String sql = "UPDATE boekingen SET datumVan=?,datumTot=?,aantalPersonen=?,verblijfskeuzeID=? WHERE boekingID=?";

        int rows = jdbcTemplate.update(sql,params,types);

        return rows;
    }

    public void deleteBoeking(int boekingID) {

        Object[] params = {boekingID};
        int[] types = {Types.INTEGER};
        String sqlDeleteBoekingDetails = "DELETE FROM boekingdetails WHERE boekingID = ?";
        String sqlDeleteBoekingen = "DELETE FROM boekingen WHERE boekingID = ?";

        jdbcTemplate.update(sqlDeleteBoekingDetails, params, types);
        jdbcTemplate.update(sqlDeleteBoekingen, params, types);
    }

    public int toevoegenReservatie(int klantID, int verblijfsKeuzeID, BigDecimal bedragVoorschot, int aantalPersonen, Date datumVan, Date datumTot, List<Integer> kamers) {
        String SqlInsertStatement = "" +
                "INSERT INTO Boekingen (KlantID, VerblijfsKeuzeID, Datum, BedragVoorschot, AantalPersonen, DatumVan, DatumTot, IsBetaald) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SqlInsertStatement, new String[]{"BoekingID"});
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

        // gets the PK of the newly created record
        int boekingID = keyHolder.getKey().intValue();

        for (int kamerID :
                kamers) {
            boekingDetailRepository.toevoegenBoekingsdetails(boekingID, kamerID);
        }

        return boekingID;
    }

    public KamerBeheer getGeboekteKamer(int kamerID){
        KamerBeheer kamer=new KamerBeheer();
        SqlRowSet rowSet= jdbcTemplate.queryForRowSet("SELECT *" +
                "from " +
                        "(((kamers  INNER JOIN BoekingDetails " +
                                "ON kamers.KAMERID = BoekingDetails.KamerID) " +
                        "INNER JOIN Boekingen " +
                            "ON Boekingen.BoekingID = BoekingDetails.BoekingID )" +
                        "INNER JOIN Prijzen " +
                            "ON Prijzen.KamerID=Kamers.kamerID )" +
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
