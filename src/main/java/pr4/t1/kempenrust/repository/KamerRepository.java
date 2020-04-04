package pr4.t1.kempenrust.repository;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.KamerType;
import pr4.t1.kempenrust.model.Prijs;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public class KamerRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ArrayList<Prijs> getAllAvailableRooms(int verblijfskeuzeID, LocalDate datumAankomst, LocalDate datumVertrek) {
        ArrayList<Prijs> kamers = new ArrayList<>();

        Date datumVan = Date.valueOf(datumAankomst);
        Date datumTot = (datumVertrek != null) ? Date.valueOf(datumVertrek) : null;

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
            prijs.setPrijsPerPersoon(rowSet.getBigDecimal("PrijsPerPersoon"));
            prijs.setDatumVanaf(rowSet.getDate("DatumVanaf"));

            kamers.add(prijs);
        }
        return kamers;
    }
}
