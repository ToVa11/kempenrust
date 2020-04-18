package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import pr4.t1.kempenrust.DTO.KamerBeheer;
import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.KamerOnbeschikbaar;
import pr4.t1.kempenrust.model.KamerType;
import pr4.t1.kempenrust.model.Prijs;
import java.util.ArrayList;
import java.util.Date;


@Repository
public class KamerRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public ArrayList<KamerBeheer> getAlleKamers(){
        ArrayList<KamerBeheer> alleKamers=new ArrayList<>();
        SqlRowSet rowSet=jdbcTemplate.queryForRowSet("SELECT *" +
                "FROM " +
                        "( Kamers INNER JOIN Kamertypes " +
                            "ON Kamers.KamerTypeID = Kamertypes.kamertypeID ) " +
                        "LEFT JOIN Kamersonbeschikbaar " +
                            "ON Kamers.kamerID=Kamersonbeschikbaar.kamerID " +
                        "ORDER BY Kamers.KamerNummer");
        while (rowSet.next()){
            KamerBeheer kamer=new KamerBeheer();
            kamer.setKamerID(rowSet.getInt("KamerID"));
            kamer.setDatumTot (rowSet.getDate("DatumTot"));
            kamer.setDatumVan(rowSet.getDate("DatumVan"));
            kamer.setKamerTypeID(rowSet.getInt("KamerTypeID"));
            kamer.setOmschrijving(rowSet.getString("Omschrijving"));
            kamer.setKamerTypeID(rowSet.getInt("KamerTypeID"));
            kamer.setKamerNummer(rowSet.getInt("KamerNummer"));
            alleKamers.add(kamer);
        }
        return alleKamers;
    }

    public void KamerToevoegen(int kamerNummer,int kamerTypeID){
        jdbcTemplate.update("INSERT INTO Kamers ( KamerNummer, KamerTypeID) " +
                "VALUES ( ?, ? )",kamerNummer,kamerTypeID );
    }
    public void  WijzigKamer(int kamerID,int kamerTypeID,int kamerNummer){
        jdbcTemplate.update("UPDATE Kamers SET KamerNummer = ? , KamerTypeID = ? " +
                "WHERE KamerID = ?",kamerNummer,kamerTypeID,kamerID);
    }
    public void KamerVerwijderen(int kamerID){
        jdbcTemplate.update("DELETE FROM Kamers WHERE KamerID =? ",kamerID);
    }

    public ArrayList<Prijs> getAlleVrijeKamers(int verblijfskeuzeID, Date datumAankomst, Date datumVertrek) {
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
                    "WHERE DatumVan >= ? AND DatumTot <= ?)",
                verblijfskeuzeID,
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
}
