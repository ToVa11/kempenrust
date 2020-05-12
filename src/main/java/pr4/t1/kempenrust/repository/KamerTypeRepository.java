package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.DTO.KamerDto;
import pr4.t1.kempenrust.model.KamerType;

import java.util.ArrayList;

@Repository
public class KamerTypeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ArrayList<KamerType>  getLijstKamerTypes(){
        ArrayList<KamerType> KamerTypes=new ArrayList<>();
        SqlRowSet rowSet=jdbcTemplate.queryForRowSet
                ("SELECT * FROM KamerTypes " +
                        "ORDER BY  KamerTypeID DESC ");
        while(rowSet.next()){
            KamerType kamerType=new KamerType();
            kamerType.setKamerTypeID(rowSet.getInt("KamerTypeID"));
            kamerType.setOmschrijving(rowSet.getString("Omschrijving"));
            KamerTypes.add(kamerType);
        }
        return KamerTypes;
    }

    public KamerDto getKamerByID(int kamerID){
        KamerDto kamer=new KamerDto();
        SqlRowSet rowSet =jdbcTemplate.queryForRowSet("SELECT * FROM " +
                "Kamers INNER JOIN Kamertypes " +
                    "ON Kamers.KamerTypeID = Kamertypes.kamertypeID " +
                "WHERE KamerID =? ",kamerID);
        while (rowSet.next()){
            kamer.setKamerID(rowSet.getInt("KamerID"));
            kamer.setKamerNummer(rowSet.getInt("KamerNummer"));
            kamer.setOmschrijving(rowSet.getString("Omschrijving"));
        }
        return kamer;
    }

    public void KamerTypeToevoegen(String omschrijving){
        jdbcTemplate.update
                ("INSERT INTO Kamertypes ( Omschrijving) " +
                      "VALUES (?)",omschrijving);
    }

}
