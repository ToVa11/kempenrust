package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.KamerOnbeschikbaar;

import java.util.ArrayList;
import java.util.Date;

@Repository
public class KamerOnbeschikbaarRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //region Queries
    public KamerOnbeschikbaar getByKamerId(int kamerID){
        Kamer kamer=new Kamer();
        KamerOnbeschikbaar onbeschikbaarKamer=new KamerOnbeschikbaar();
    SqlRowSet rowSet= jdbcTemplate.queryForRowSet("SELECT * " +
                "from" +
                "     kamers  LEFT JOIN  KAMERSONBESCHIKBAAR " +
                "     on kamers.KAMERID = KAMERSONBESCHIKBAAR.KAMERID " +
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

    public ArrayList<KamerOnbeschikbaar> getTussenTweeDatums(java.sql.Date van, java.sql.Date tot) {
        ArrayList<KamerOnbeschikbaar> lijstKamerOnbeschikbaar = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("" +
                "SELECT * " +
                "FROM Kamers INNER JOIN KamersOnbeschikbaar " +
                    "ON Kamers.KamerID = KamersOnbeschikbaar.KamerID " +
                "WHERE ? BETWEEN DatumVan AND DatumTot " +
                    "OR ? BETWEEN DatumVan AND DatumTot " +
                    "OR DatumVan BETWEEN ? AND ? " +
                    "OR DatumTot BETWEEN ? AND ?",
                van, tot,
                van, tot,
                van, tot);

        while(rowSet.next()) {
            KamerOnbeschikbaar kamerOnbeschikbaar = new KamerOnbeschikbaar();
            Kamer kamer = new Kamer();

            kamerOnbeschikbaar.setKamerOnbeschikbaarID(rowSet.getInt("KamersOnbeschikbaarID"));
            kamerOnbeschikbaar.setKamerID(rowSet.getInt("KamerID"));
            kamerOnbeschikbaar.setDatumVan(rowSet.getDate("DatumVan"));
            kamerOnbeschikbaar.setDatumTot(rowSet.getDate("DatumTot"));

            kamer.setKamerID(rowSet.getInt("KamerID"));
            kamer.setKamerNummer(rowSet.getInt("KamerNummer"));
            kamerOnbeschikbaar.setKamer(kamer);

            lijstKamerOnbeschikbaar.add(kamerOnbeschikbaar);
        }

        return lijstKamerOnbeschikbaar;
    }
    //endregion

    //region Commands
    public void create(int kamerID, Date datumVan, Date datumTot){
        jdbcTemplate.update("INSERT INTO KamersOnbeschikbaar (KamerID, DatumVan, DatumTot)" +
                " VALUES (?, ?, ?)",kamerID,datumVan,datumTot);
    }

    public void update(int kamerId, Date datumVan, Date datumTot){
        jdbcTemplate.update("UPDATE KamersOnbeschikbaar SET DatumVan = ? , DatumTot = ? " +
                " WHERE KamerID = ? ", datumVan, datumTot, kamerId);
    }

    public void delete(int kamerID){
        jdbcTemplate.update("DELETE FROM KAMERSONBESCHIKBAAR WHERE KamerID =? ",kamerID);
    }
    //endregion
}
