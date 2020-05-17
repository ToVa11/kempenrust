package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.model.VerblijfsKeuze;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;

@Repository
public class VerblijfsKeuzeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //region Queries
    public ArrayList<VerblijfsKeuze> get() {

        ArrayList<VerblijfsKeuze> verblijfsKeuzes = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                    "FROM VerblijfsKeuzes");

        while (rowSet.next()) {
            VerblijfsKeuze keuze = new VerblijfsKeuze();

            keuze.setVerblijfskeuzeID(rowSet.getInt("VerblijfsKeuzeID"));
            keuze.setNaam(rowSet.getString("Naam"));
            keuze.setOmschrijving(rowSet.getString("Omschrijving"));

            verblijfsKeuzes.add(keuze);
        }

        return verblijfsKeuzes;
    }

    public VerblijfsKeuze getById(int verblijfskeuzeID) {
        VerblijfsKeuze keuze = new VerblijfsKeuze();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("" +
                "SELECT * " +
                "FROM Verblijfskeuzes " +
                "WHERE " +
                "verblijfskeuzeID = ?",
                verblijfskeuzeID);

        while(rowSet.next()) {
            keuze.setVerblijfskeuzeID(rowSet.getInt("VerblijfsKeuzeID"));
            keuze.setNaam(rowSet.getString("Naam"));
            keuze.setOmschrijving(rowSet.getString("Omschrijving"));
        }

        return keuze;
    }
    //endregion

    //region Commands
    public int create(VerblijfsKeuze verblijfsKeuze) {
        String sqlInsertStatement = "" +
                "INSERT INTO verblijfskeuzes " +
                "(naam, omschrijving) " +
                "VALUES " +
                "( ? , ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sqlInsertStatement, new String[]{"verblijfskeuzeID"});
            ps.setString(1, verblijfsKeuze.getNaam());
            ps.setString(2, verblijfsKeuze.getOmschrijving());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue(); //returns new verblijfskeuzeID
    }

    public int update(VerblijfsKeuze verblijfsKeuze) {
        Object[] params = {verblijfsKeuze.getNaam(), verblijfsKeuze.getOmschrijving(), verblijfsKeuze.getVerblijfskeuzeID()};
        int[] types = {Types.VARCHAR, Types.VARCHAR, Types.INTEGER};

        String sql = "UPDATE verblijfskeuzes SET naam = ?, omschrijving = ? " +
                "WHERE verblijfskeuzeID = ? ";

        return jdbcTemplate.update(sql,params,types);
    }

    public void delete(int verblijfskeuzeID) {

        Object[] params = {verblijfskeuzeID};
        int[] types = {Types.INTEGER};
        String sqlVerwijderVerblijfskeuze = "DELETE FROM verblijfskeuzes WHERE verblijfskeuzeID = ?";

        jdbcTemplate.update(sqlVerwijderVerblijfskeuze, params, types);

    }
    //endregion
}
