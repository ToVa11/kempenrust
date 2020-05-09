package pr4.t1.kempenrust.model.DTO;

import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.Prijs;
import pr4.t1.kempenrust.model.VerblijfsKeuze;

import java.math.BigDecimal;
import java.util.List;

public class arrangementDTO {
    private VerblijfsKeuze verblijfsKeuze;
    private List<Kamer> kamers;
    private List<Prijs> kamerPrijzen;

    public VerblijfsKeuze getVerblijfsKeuze() {
        return verblijfsKeuze;
    }

    public void setVerblijfsKeuze(VerblijfsKeuze verblijfsKeuze) {
        this.verblijfsKeuze = verblijfsKeuze;
    }

    public List<Kamer> getKamers() {
        return kamers;
    }

    public void setKamers(List<Kamer> kamers) {
        this.kamers = kamers;
    }

    public List<Prijs> getKamerPrijzen() {
        return kamerPrijzen;
    }

    public void setKamerPrijzen(List<Prijs> kamerPrijzen) {
        this.kamerPrijzen = kamerPrijzen;
    }
}
