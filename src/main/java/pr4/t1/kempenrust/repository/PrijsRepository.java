package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.DTO.KamerDto;
import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.KamerType;
import pr4.t1.kempenrust.model.Prijs;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


@Repository
public class PrijsRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void prijsVerwijderen(int kamerID){
        jdbcTemplate.update("DELETE FROM Prijzen WHERE KamerID =? ",kamerID);
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

    public int voegPrijsToeVoorVerblijfskeuze(List<Prijs> kamerPrijzen, int verblijfskeuzeID) {
        int rowsInserted=0;
        for (Prijs kamerPrijs: kamerPrijzen) {
            String sqlInsertStatement = "" +
                    "INSERT INTO prijzen " +
                    "(kamerID, verblijfskeuzeID, prijsPerKamer, datumVanaf) " +
                    "VALUES " +
                    "( ? , ?, ?, ? )";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        sqlInsertStatement, new String[]{"verblijfskeuzeID"});
                ps.setInt(1, kamerPrijs.getKamerID());
                ps.setInt(2, verblijfskeuzeID);
                ps.setBigDecimal(3,kamerPrijs.getPrijsPerKamer());
                ps.setDate(4, new Date(kamerPrijs.getDatumVanaf().getTime()));
                return ps;
            }, keyHolder);

            if(keyHolder.getKey().intValue() >0) {
                rowsInserted++;
            }
        }
        return rowsInserted;
    }

    public void deletePrijsVoorVerblijfskeuze(int verblijfskeuzeID) {
        Object[] params = {verblijfskeuzeID};
        int[] types = {Types.INTEGER};
        String sqlVerwijderPrijzen = "DELETE FROM prijzen WHERE verblijfskeuzeID = ?";

        jdbcTemplate.update(sqlVerwijderPrijzen, params, types);
    }

    public List<Prijs> getPrijzenVoorVerblijfskeuze(int verblijfskeuzeID) {
        ArrayList<Prijs> prijzen = new ArrayList<>();
        String query = "" +
                "SELECT * " +
                "FROM Prijzen INNER JOIN Kamers " +
                "ON Prijzen.KamerID = Kamers.KamerID " +
                "WHERE Prijzen.verblijfskeuzeID = ? ";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query, verblijfskeuzeID);

        while (rowSet.next()) {
            Prijs prijs = new Prijs();
            Kamer kamer = new Kamer();

            kamer.setKamerID(rowSet.getInt("kamerID"));
            kamer.setKamerNummer(rowSet.getInt("kamerNummer"));

            prijs.setPrijsID(rowSet.getInt("prijsID"));
            prijs.setVerblijfsKeuzeID(verblijfskeuzeID);
            prijs.setKamer(kamer);
            prijs.setDatumVanaf(rowSet.getDate("datumVanaf"));
            prijs.setPrijsPerKamer(rowSet.getBigDecimal("prijsPerKamer"));

            prijzen.add(prijs);
        }

        return prijzen;
    }

    public int updatePrijzenVoorVerblijfskeuze(List<Prijs> kamerPrijzenMetDatums) {
        int rowsUpdated = 0;
        for (Prijs prijs: kamerPrijzenMetDatums) {
            Object[] params = {prijs.getDatumVanaf(), prijs.getPrijsPerKamer(), prijs.getPrijsID()};
            int[] types = {Types.DATE,Types.DECIMAL, Types.INTEGER};

            String sql = "" +
                    "UPDATE prijzen " +
                    "SET datumVanaf = ?, " +
                    "prijsPerKamer = ? " +
                    "WHERE " +
                    "prijsID = ? ";

            rowsUpdated += jdbcTemplate.update(sql, params, types);
        }
        return rowsUpdated;
    }
}
