package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.DTO.KamerBeheer;
import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.KamerOnbeschikbaar;
import pr4.t1.kempenrust.model.KamerType;

import java.util.ArrayList;
import java.util.Date;

@Repository
public class KamerRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public ArrayList<KamerBeheer> AlleKamers(){
        ArrayList<KamerBeheer> alleKamers=new ArrayList<>();
        SqlRowSet rowSet=jdbcTemplate.queryForRowSet("SELECT *" +
                "FROM" +
                "(" +
                "(" +
                "Kamers INNER JOIN Kamertypes " +
                "ON Kamers.KamerTypeID = Kamertypes.kamertypeID" +
                ")" +
                "LEFT JOIN Kamersonbeschikbaar " +
                "ON Kamers.kamerID=Kamersonbeschikbaar.kamerID" +
                ")" +
                "ORDER BY Kamers.KamerNummer");
        while (rowSet.next()){
            KamerBeheer kamer=new KamerBeheer();
            kamer.setKamerID(rowSet.getInt("KamerID"));
            kamer.setDatumTot (rowSet.getDate("DatumTot"));
            kamer.setDatumVan(rowSet.getDate("DatumVan"));
            kamer.setKamerTypeID(rowSet.getInt(rowSet.getInt("KamerTypeID")));
            kamer.setOmschrijving(rowSet.getString("Omschrijving"));
            kamer.setKamerTypeID(rowSet.getInt("KamerTypeID"));
            kamer.setKamerNummer(rowSet.getInt("KamerNummer"));
            alleKamers.add(kamer);
        }
        return alleKamers;
    }

    public void KamerToevoegen(int kamerNummer,int kamerTypeID){
        jdbcTemplate.update("Insert INTO Kamers (KamerTypeID,KamerNummer) " +
                "VALUES (?,?)",kamerTypeID,kamerNummer);
    }
    public void  WijzigKamer(int kamerID,int kamerTypeID,int kamerNummer){
        jdbcTemplate.update("UPDATE Kamers SET KamerNummer = ? , KamerTypeID = ? " +
                "WHERE KamerID = ?",kamerNummer,kamerTypeID,kamerID);
    }
    public void KamerVerwijderen(int kamerID){
        jdbcTemplate.update("DELETE FROM Kamers WHERE KamerID =? ",kamerID);
    }

}
