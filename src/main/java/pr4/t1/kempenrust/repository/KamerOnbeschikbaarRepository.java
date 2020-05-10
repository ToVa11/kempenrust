package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.DTO.KamerDto;
import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.KamerOnbeschikbaar;

import java.util.Date;

@Repository
public class KamerOnbeschikbaarRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public KamerOnbeschikbaar getOnbeschikbaarKamerByID(int kamerID){
        Kamer kamer=new Kamer();
        KamerOnbeschikbaar onbeschikbaarKamer=new KamerOnbeschikbaar();
    SqlRowSet rowSet= jdbcTemplate.queryForRowSet("SELECT * " +
                "from" +
                "(" +
                "     kamers  LEFT JOIN  KAMERSONBESCHIKBAAR " +
                "    on kamers.KAMERID = KAMERSONBESCHIKBAAR.KAMERID" +
                ")" +
                "where Kamers.kamerID = ? ",kamerID);
    while(rowSet.next()){
        kamer.setKamerNummer(rowSet.getInt("KamerNummer"));
        onbeschikbaarKamer.setKamer(kamer);
        onbeschikbaarKamer.setKamerID(rowSet.getInt("KamerID"));
        onbeschikbaarKamer.setKamerOnbeschikbaarID(rowSet.getInt("KamersOnbeschikbaarID"));
        onbeschikbaarKamer.setDatumVan(rowSet.getDate("DatumVan"));
        onbeschikbaarKamer.setDatumTot(rowSet.getDate("DatumTot"));

    }
    return  onbeschikbaarKamer;
    }


    public void KamerOnbechikbaarMaken(int kamerID, Date datumVan,Date datumTot){
        jdbcTemplate.update("INSERT INTO KamersOnbeschikbaar (KamerID, DatumVan, DatumTot)" +
                " VALUES (?, ?, ?)",kamerID,datumVan,datumTot);
    }
    public void  wijzigOnbeschikbaarheid(int kamerId, Date datumVan, Date datumTot){
        jdbcTemplate.update("UPDATE KamersOnbeschikbaar SET DatumVan = ? , DatumTot = ? " +
                " WHERE KamerID = ? ", datumVan, datumTot, kamerId);
    }
    public void KamerBeschikbaarMaken(int kamerID){
        jdbcTemplate.update("DELETE FROM KAMERSONBESCHIKBAAR WHERE KamerID =? ",kamerID);

    }

}
