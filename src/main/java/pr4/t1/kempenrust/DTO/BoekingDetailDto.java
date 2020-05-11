package pr4.t1.kempenrust.DTO;

import org.springframework.format.annotation.DateTimeFormat;
import pr4.t1.kempenrust.model.Boeking;
import pr4.t1.kempenrust.model.Kamer;

import java.util.Date;

public class BoekingDetailDto {
    private String datumVan;
    private String datumTot;

    public String getDatumVan() {
        return datumVan;
    }

    public void setDatumVan(String datumVan) {
        this.datumVan = datumVan;
    }

    public String getDatumTot() {
        return datumTot;
    }

    public void setDatumTot(String datumTot) {
        this.datumTot = datumTot;
    }

}
