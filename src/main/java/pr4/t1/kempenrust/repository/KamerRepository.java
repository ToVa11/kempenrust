package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import pr4.t1.kempenrust.model.*;
import pr4.t1.kempenrust.model.DTO.KamerDto;

import java.util.ArrayList;
import java.util.Date;


@Repository
public class KamerRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //region Queries
    public ArrayList<Kamer> get(){
        ArrayList<Kamer> alleKamers=new ArrayList<>();
        SqlRowSet rowSet=jdbcTemplate.queryForRowSet("" +
                "SELECT * " +
                "FROM Kamers " +
                "ORDER BY Kamers.KamerNummer");
        while (rowSet.next()){
            var kamer=new Kamer();
            kamer.setKamerID(rowSet.getInt("KamerID"));
            kamer.setKamerTypeID(rowSet.getInt("KamerTypeID"));
            kamer.setKamerTypeID(rowSet.getInt("KamerTypeID"));
            kamer.setKamerNummer(rowSet.getInt("KamerNummer"));
            alleKamers.add(kamer);
        }
        return alleKamers;
    }

    public ArrayList<KamerOnbeschikbaar> getWithKamerOnbeschikbaar(){
        ArrayList<KamerOnbeschikbaar> lijstKamers=new ArrayList<>();
        SqlRowSet rowSet=jdbcTemplate.queryForRowSet("SELECT *" +
                "FROM " +
                "( Kamers INNER JOIN Kamertypes " +
                "ON Kamers.KamerTypeID = Kamertypes.kamertypeID ) " +
                "LEFT JOIN Kamersonbeschikbaar " +
                "ON Kamers.kamerID=Kamersonbeschikbaar.kamerID " +
                "ORDER BY Kamers.KamerNummer");
        while (rowSet.next()){
            KamerOnbeschikbaar kamerOnb=new KamerOnbeschikbaar();
            Kamer kamer= new Kamer();
            KamerType kamerType= new KamerType();

            kamerType.setOmschrijving(rowSet.getString("Omschrijving"));

            kamer.setKamerType(kamerType);
            kamer.setKamerTypeID(rowSet.getInt("KamerTypeID"));
            kamer.setKamerNummer(rowSet.getInt("KamerNummer"));

            kamerOnb.setKamer(kamer);
            kamerOnb.setKamerID(rowSet.getInt("KamerID"));
            kamerOnb.setDatumTot (rowSet.getDate("DatumTot"));
            kamerOnb.setDatumVan(rowSet.getDate("DatumVan"));

            lijstKamers.add(kamerOnb);

        }
        return lijstKamers;
    }

    public Kamer getById(int kamerID){
        Kamer kamer=new Kamer();
        KamerType kamerType=new KamerType();
        SqlRowSet rowSet =jdbcTemplate.queryForRowSet("SELECT * FROM " +
                "Kamers INNER JOIN KamerTypes " +
                "ON Kamers.KamerTypeID = KamerTypes.KamerTypeID " +
                "WHERE KamerID =? ",kamerID);
        while (rowSet.next()){

            kamerType.setOmschrijving(rowSet.getString("Omschrijving"));

            kamer.setKamerType(kamerType);
            kamer.setKamerID(rowSet.getInt("KamerID"));
            kamer.setKamerNummer(rowSet.getInt("KamerNummer"));
        }
        return kamer;
    }
    
    public boolean checkIfKamernummerExists(int kamerNummer){
        SqlRowSet rowSet= jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM Kamers " +
                " WHERE Kamers.KamerNummer = ? ",kamerNummer);

        // false = no records in rowSet = kamernummer doesnt exists
        // true = record in rowSet = kamernummer exists
        return rowSet.next();
    }

    public ArrayList<Prijs> getByBeschikbaarheid(int verblijfskeuzeID, Date datumAankomst, Date datumVertrek) {
        ArrayList<Prijs> prijzenKamers = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("" +
                        "SELECT * " +
                        "FROM Prijzen INNER JOIN Kamers " +
                        "ON Prijzen.KamerID = Kamers.KamerID " +
                        "INNER JOIN KamerTypes " +
                        "ON Kamers.KamerTypeID = KamerTypes.KamerTypeID " +
                        "WHERE VerblijfsKeuzeID = ? AND Kamers.KamerID NOT IN " +
                        "(SELECT BoekingDetails.KamerID " +
                        "FROM BoekingDetails INNER JOIN Boekingen " +
                        "ON BoekingDetails.BoekingID = Boekingen.BoekingID " +
                        "WHERE ? BETWEEN DatumVan AND DatumTot " +
                        " OR ? BETWEEN DatumVan AND DatumTot " +
                        " OR DatumVan BETWEEN  ? AND ? " +
                        " OR DatumTot BETWEEN ? AND ? " +
                        ") " +
                        "AND Kamers.KamerID NOT IN " +
                        "(SELECT KamersOnbeschikbaar.KamerID " +
                        "FROM KamersOnbeschikbaar " +
                        "WHERE ? BETWEEN DatumVan AND DatumTot " +
                        "OR ? BETWEEN DatumVan AND DatumTot " +
                        ")",
                verblijfskeuzeID,
                datumAankomst,
                datumVertrek,
                datumAankomst,
                datumVertrek,
                datumAankomst,
                datumVertrek,
                datumAankomst,
                datumVertrek);

        while (rowSet.next()) {
            Kamer kamer = new Kamer();
            KamerType kamerType = new KamerType();
            Prijs prijs = new Prijs();
            kamerType.setKamerTypeID(rowSet.getInt("KamerTypeID"));
            kamerType.setOmschrijving(rowSet.getString("Omschrijving"));
            kamer.setKamerID(rowSet.getInt("KamerID"));
            kamer.setKamerTypeID(rowSet.getInt("KamerTypeID"));
            kamer.setKamerType(kamerType);
            kamer.setKamerNummer(rowSet.getInt("KamerNummer"));
            prijs.setPrijsID(rowSet.getInt("PrijsID"));
            prijs.setKamerID(rowSet.getInt("KamerID"));
            prijs.setKamer(kamer);
            prijs.setVerblijfsKeuzeID(rowSet.getInt("VerblijfsKeuzeID"));
            prijs.setPrijsPerKamer(rowSet.getBigDecimal("PrijsPerKamer"));
            prijs.setDatumVanaf(rowSet.getDate("DatumVanaf"));

            prijzenKamers.add(prijs);
        }
        return prijzenKamers;
    }
    //endregion

    //region Commands
    public void create(int kamerNummer, int kamerTypeID){
        jdbcTemplate.update("INSERT INTO Kamers ( KamerNummer, KamerTypeID) " +
                "VALUES ( ?, ? )",kamerNummer,kamerTypeID );
    }

    public void update(int kamerID, int kamerTypeID, int kamerNummer){
        jdbcTemplate.update("UPDATE Kamers SET KamerNummer = ? , KamerTypeID = ? " +
                "WHERE KamerID = ?",kamerNummer,kamerTypeID,kamerID);
    }

    public void delete(int kamerID){
        jdbcTemplate.update("DELETE FROM Kamers WHERE KamerID =? ",kamerID);
    }
    //endregion
}
