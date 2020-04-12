package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.DTO.KamerBeheer;
import pr4.t1.kempenrust.model.KamerOnbeschikbaar;

import java.util.Date;

@Repository
public class KamerOnbeschikbaarRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public KamerBeheer OnbKamerDoorID(int kamerID){
        KamerBeheer kamer=new KamerBeheer();
    SqlRowSet rowSet= jdbcTemplate.queryForRowSet("SELECT * " +
                "from" +
                "(" +
                "     kamers  LEFT JOIN  KAMERSONBESCHIKBAAR " +
                "    on kamers.KAMERID = KAMERSONBESCHIKBAAR.KAMERID" +
                ")" +
                "where Kamers.kamerID = ? ",kamerID);
    while(rowSet.next()){

        kamer.setKamerOnbeschikbaarID(rowSet.getInt("KamersOnbeschikbaarID"));
        kamer.setDatumVan(rowSet.getDate("DatumVan"));
        kamer.setDatumTot(rowSet.getDate("DatumTot"));
        kamer.setKamerID(rowSet.getInt("KamerID"));
        kamer.setKamerNummer(rowSet.getInt("KamerNummer"));
    }
    return  kamer;
    }
    public KamerBeheer VerOnbKamerDoorID(int kamerID){
        KamerBeheer kamer=new KamerBeheer();
        SqlRowSet rowSet= jdbcTemplate.queryForRowSet("SELECT *" +
                "FROM" +
                "(" +
                "     kamers  INNER JOIN KAMERSONBESCHIKBAAR " +
                "    ON kamers.KAMERID = KAMERSONBESCHIKBAAR.KAMERID" +
                ")" +
                "WHERE Kamers.kamerID = ? ",kamerID);
        while(rowSet.next()){

            kamer.setKamerOnbeschikbaarID(rowSet.getInt("KamersOnbeschikbaarID"));
            kamer.setDatumVan(rowSet.getDate("DatumVan"));
            kamer.setDatumTot(rowSet.getDate("DatumTot"));
            kamer.setKamerID(rowSet.getInt("KamerID"));
            kamer.setKamerNummer(rowSet.getInt("KamerNummer"));
            kamer.setBeschikbaarheid(false);
            beschikbaarheidVerwijderen(kamerID);
        }
        return  kamer;
    }

    public void KamerOnbechikbaarMaken(int kamerID, Date datumVan,Date datumTot){
        jdbcTemplate.update("INSERT INTO KamersOnbeschikbaar (KamerID,DatumVan,DatumTot)" +
                " VALUES (?, ?, ?)",kamerID,datumVan,datumTot);
    }
    public void  OnbeschikbaarKamerWijzig(int kamersOnbeschikbaarID,Date datumVan,Date datumTot){
        jdbcTemplate.update("UPDATE KamersOnbeschikbaar SET DatumVan = ? AND DatumTot = ?" +
                " WHERE KamersOnbeschikbaarID = ?",kamersOnbeschikbaarID,datumVan,datumTot);
    }
    public void KamerBeschikbaarMaken(int kamerID){
        jdbcTemplate.update("DELETE FROM KAMERSONBESCHIKBAAR WHERE KamerID =? ",kamerID);

    }
    public void beschikbaarheidVerwijderen(int kamerID){
        jdbcTemplate.update("DELETE FROM KamersOnbeschikbaar WHERE KamerID =? ",kamerID);
    }

}
