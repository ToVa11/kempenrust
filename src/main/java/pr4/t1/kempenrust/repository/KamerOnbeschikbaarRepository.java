package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.DTO.KamerBeheer;
import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.KamerOnbeschikbaar;

import java.util.ArrayList;
import java.util.Date;

@Repository
public class KamerOnbeschikbaarRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public KamerBeheer getOnbeschikbaarKamerByID(int kamerID){
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

    public ArrayList<KamerOnbeschikbaar> getOnbeschikbaarKamerTussenTweeDatums(java.sql.Date van, java.sql.Date tot) {
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

    public KamerBeheer maakKamerBeschikbaarByID(int kamerID){
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
            KamerBeschikbaarMaken(kamerID);
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

}
