package pr4.t1.kempenrust.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import pr4.t1.kempenrust.DTO.KamerBeheer;

@Repository
public class BoekingDetailRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

}
