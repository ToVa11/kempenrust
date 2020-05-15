package pr4.t1.kempenrust.model.DTO;

import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.Prijs;
import pr4.t1.kempenrust.model.VerblijfsKeuze;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ArrangementDTO {
    private VerblijfsKeuze verblijfsKeuze;
    private List<String> datums;
    private List<Prijs> kamerPrijzen;

    public VerblijfsKeuze getVerblijfsKeuze() {
        return verblijfsKeuze;
    }

    public void setVerblijfsKeuze(VerblijfsKeuze verblijfsKeuze) {
        this.verblijfsKeuze = verblijfsKeuze;
    }

    public List<String> getDatums() {
        return datums;
    }

    public void setDatums(List<String> datums) {
        this.datums = datums;
    }

    public List<Prijs> getKamerPrijzen() {
        return kamerPrijzen;
    }

    public void setKamerPrijzen(List<Prijs> kamerPrijzen) {
        this.kamerPrijzen = kamerPrijzen;
    }

}
