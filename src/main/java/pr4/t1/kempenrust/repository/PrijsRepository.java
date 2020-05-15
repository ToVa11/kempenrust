package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.DTO.KamerDto;
import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.KamerType;
import pr4.t1.kempenrust.model.Prijs;

import java.util.ArrayList;
import java.util.List;


@Repository
public class PrijsRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public void prijsVerwijderen(int kamerID){
        jdbcTemplate.update("DELETE FROM Prijzen WHERE KamerID =? ",kamerID);
    }
    public KamerDto kamerTeVerwijderen(int kamerID){
        KamerDto kamer=new KamerDto();
        SqlRowSet rowSet= jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM " +
                "kamers  INNER JOIN Prijzen " +
                    "ON kamers.KAMERID = Prijzen.KAMERID " +
                "WHERE Kamers.kamerID = ? ",kamerID);
        while(rowSet.next()){
            kamer.setKamerID(rowSet.getInt("KamerID"));
            prijsVerwijderen(kamerID);
        }
        return  kamer;
    }

    public ArrayList<Prijs> GetPrijzenVoorReservatie(int verblijfsKeuzeId, List<Integer> kamers) {
        ArrayList<Prijs> prijzenKamers = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                queryPrijzenVoorReservatie(kamers),
                verblijfsKeuzeId);

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

    private String queryPrijzenVoorReservatie(List<Integer> kamers) {
        String query = "" +
                "SELECT * " +
                "FROM Prijzen INNER JOIN Kamers " +
                "ON Prijzen.KamerID = Kamers.KamerID " +
                "INNER JOIN KamerTypes " +
                "ON Kamers.KamerTypeID = KamerTypes.KamerTypeID " +
                "WHERE VerblijfsKeuzeID = ? AND Kamers.KamerID IN (";

        StringBuilder queryBuilder = new StringBuilder(query);
        for (int i = 0; i < kamers.size(); i++) {
            queryBuilder.append(kamers.get(i));
            if(i != kamers.size() -1)
                queryBuilder.append(", ");
        }
        queryBuilder.append(")");

        return queryBuilder.toString();
    }
}
