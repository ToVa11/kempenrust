package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.model.Boeking;
import pr4.t1.kempenrust.model.Klant;
import pr4.t1.kempenrust.model.VerblijfsKeuze;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class BoekingRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
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

    public int toevoegenReservatie(int klantID, int verblijfsKeuzeID, BigDecimal bedragVoorschot, int aantalPersonen, Date datumVan, Date datumTot, List<Integer> kamers) {
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

        // gets the PK of the newly created record
        int boekingID =  keyHolder.getKey().intValue();

        for (int kamerID:
             kamers) {
            boekingDetailRepository.toevoegenBoekingsdetails(boekingID, kamerID);
        }

        return boekingID;
    }
}
