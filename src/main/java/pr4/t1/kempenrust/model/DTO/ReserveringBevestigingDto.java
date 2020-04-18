package pr4.t1.kempenrust.model.DTO;

import pr4.t1.kempenrust.model.Boeking;
import pr4.t1.kempenrust.model.Klant;
import pr4.t1.kempenrust.model.Prijs;
import pr4.t1.kempenrust.model.VerblijfsKeuze;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ReserveringBevestigingDto {
    private Boeking boeking;
    private List<Prijs> prijzenKamers;
    private BigDecimal totaalPrijs;

    public ReserveringBevestigingDto() {
    }

    public Boeking getBoeking() {
        return boeking;
    }

    public void setBoeking(Boeking boeking) {
        this.boeking = boeking;
    }

    public List<Prijs> getPrijzenKamers() {
        return prijzenKamers;
    }

    public void setPrijzenKamers(List<Prijs> prijzenKamers) {
        this.prijzenKamers = prijzenKamers;
    }

    public BigDecimal getTotaalPrijs() {
        return totaalPrijs;
    }

    public void setTotaalPrijs(BigDecimal totaalPrijs) {
        this.totaalPrijs = totaalPrijs;
    }
}
