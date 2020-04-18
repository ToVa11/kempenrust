package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.model.Boeking;
import pr4.t1.kempenrust.model.Klant;

import java.sql.Date;
import java.sql.Types;

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

    public int updateBoeking(String datumVan, String datumTot, int aantalPersonen, int verblijfskeuzeID, int boekingID){
        Object[] params = {Date.valueOf(datumVan),Date.valueOf(datumTot),aantalPersonen,verblijfskeuzeID,boekingID};
        int[] types = {Types.DATE,Types.DATE,Types.INTEGER,Types.INTEGER,Types.INTEGER};

        String sql = "UPDATE boekingen SET datumVan=?,datumTot=?,aantalPersonen=?,verblijfskeuzeID=? WHERE boekingID=?";

        int rows = jdbcTemplate.update(sql,params,types);

        return rows;
    }

    public void deleteBoeking(int boekingID) {

        Object[] params = { boekingID };
        int[] types = {Types.INTEGER};
        String sqlDeleteBoekingDetails = "DELETE FROM boekingdetails WHERE boekingID = ?";
        String sqlDeleteBoekingen = "DELETE FROM boekingen WHERE boekingID = ?";

        jdbcTemplate.update(sqlDeleteBoekingDetails,params,types);
        jdbcTemplate.update(sqlDeleteBoekingen,params,types);
    }
}
