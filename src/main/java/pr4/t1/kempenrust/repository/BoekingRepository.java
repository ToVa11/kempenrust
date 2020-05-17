package pr4.t1.kempenrust.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.model.*;
import java.sql.Date;
import java.sql.Types;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import static java.lang.Boolean.TRUE;

@Repository
public class BoekingRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BoekingDetailRepository boekingDetailRepository;

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

    public int updateBoeking(int aantalPersonen, int verblijfskeuzeID, int boekingID){
        Object[] params = {aantalPersonen,verblijfskeuzeID,boekingID};
        int[] types = {Types.INTEGER,Types.INTEGER,Types.INTEGER};

        String sql = "UPDATE boekingen SET aantalPersonen=?,verblijfskeuzeID=? WHERE boekingID=?";

        int rows = jdbcTemplate.update(sql,params,types);

        return rows;
    }

    public int updateBoekingDatums(Date datumVan, Date datumTot, int boekingID) {
        Object[] params = {datumVan, datumTot, boekingID};
        int[] types = {Types.DATE, Types.DATE, Types.INTEGER};

        String sql = "UPDATE boekingen SET datumVan=?,datumTot=? WHERE boekingID=?";

        int rows = jdbcTemplate.update(sql, params, types);

        return rows;
    }

    public int voegKamerToeAanBoeking(int boekingID, List<Integer> kamerIDs) {
        int rows=0;
        for (int kamerID:kamerIDs){
            Object[] params = {boekingID,kamerID};
            int[] types = {Types.INTEGER,Types.INTEGER};

            String sql = "INSERT INTO boekingDetails (" +
                    "boekingID, " +
                    "kamerID" +
                    ") VALUES(" +
                    "? , " +
                    "?)";

            rows += jdbcTemplate.update(sql,params,types);
        }

        return rows;
    }

    public int verwijderKamerVanBoeking(int boekingID, List<Integer> kamerIDs) {
        int rows=0;

        for (int kamerID: kamerIDs) {
            Object[] params = {boekingID, kamerID};
            int[] types = {Types.INTEGER, Types.INTEGER};

            String sql = "DELETE FROM boekingDetails WHERE boekingID=? AND kamerID=?";

            rows += jdbcTemplate.update(sql,params,types);
        }

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

    public Boeking getBoekingVoorKamer(int kamerID){
        Boeking boeking=new Boeking();
        SqlRowSet rowSet= jdbcTemplate.queryForRowSet("SELECT *" +
                "from " +
                        "((kamers  INNER JOIN BoekingDetails " +
                                "ON kamers.KAMERID = BoekingDetails.KamerID) " +
                        "INNER JOIN Boekingen " +
                            "ON Boekingen.BoekingID = BoekingDetails.BoekingID ) " +
                "WHERE Kamers.kamerID = ? ",kamerID);
        while(rowSet.next()){
            boeking.setDatumVan(rowSet.getDate("DatumVan"));
            boeking.setDatumTot(rowSet.getDate("DatumTot"));
        }
        return  boeking;
    }

    public Boeking getBoekingVoorKlant(int klantId) {
        Boeking boeking = new Boeking();
        Klant klant=new Klant();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(

                "SELECT * FROM  " +
                        "boekingen INNER JOIN klanten ON boekingen.klantId = klanten.klantId " +
                        "WHERE " +
                        "boekingen.KlantID = '"+klantId+"'" +
                        "AND Boekingen.DatumTot > '"+Date.valueOf(LocalDate.now())+"' ");


        while (rowSet.next()) {
            klant.setNaam(rowSet.getString("naam"));
            klant.setVoornaam(rowSet.getString("voornaam"));

            boeking.setKlant(klant);
            boeking.setDatumVan(rowSet.getDate("DatumVan"));
            boeking.setDatumTot(rowSet.getDate("DatumTot"));


        }
        return boeking;
    }

    public List<Boeking> getBoekingenMetOnbetaaldVoorschot() {
        List<Boeking> boekingen = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("" +
                "SELECT * FROM " +
                "Boekingen INNER JOIN Klanten " +
                "ON Boekingen.KlantID = Klanten.KlantID " +
                "WHERE " +
                "BedragVoorschot > 0 AND " +
                "IsBetaald=FALSE " +
                "ORDER BY DatumVan"
        );

        while (rowSet.next()) {
            Boeking boeking = new Boeking();
            Klant klant = new Klant();

            klant.setNaam(rowSet.getString("naam"));
            klant.setVoornaam(rowSet.getString("voornaam"));

            boeking.setBoekingID(rowSet.getInt("boekingID"));
            boeking.setDatumVan(rowSet.getDate("datumVan"));
            boeking.setDatumTot(rowSet.getDate("datumTot"));
            boeking.setBedragVoorschot(rowSet.getBigDecimal("bedragVoorschot"));

            boeking.setKlant(klant);

            boekingen.add(boeking);
        }
        return boekingen;
    }

    public int bevestigVoorschot(String boekingID) {
        Object[] params = {TRUE, boekingID};
        int[] types = {Types.BOOLEAN, Types.INTEGER};

        String sql = "UPDATE Boekingen SET isBetaald = ? WHERE boekingID = ?";

        int rowsUpdated = jdbcTemplate.update(sql,params,types);

        return rowsUpdated;
    }

    public int getAantalBoekingenVoorVerblijfskeuze(int verblijfskeuzeID) {
        return jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM Boekingen " +
                "WHERE verblijfskeuzeID = ? AND datumVan >= ? ", new Object[] { verblijfskeuzeID, new Date(System.currentTimeMillis()) }, Integer.class);
    }
}
