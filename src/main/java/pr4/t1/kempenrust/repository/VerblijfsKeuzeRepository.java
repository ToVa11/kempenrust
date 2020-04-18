package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.model.VerblijfsKeuze;

import java.util.ArrayList;

@Repository
public class VerblijfsKeuzeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ArrayList<VerblijfsKeuze> getAlleVerblijfsKeuzes() {

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

}
