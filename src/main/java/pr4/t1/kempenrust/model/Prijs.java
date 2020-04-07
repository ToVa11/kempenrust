package pr4.t1.kempenrust.model;

import java.math.BigDecimal;
import java.util.Date;

public class Prijs {
    private int prijsID;
    private BigDecimal prijsPerKamer;
    private Date datumVanaf;
    private int kamerID;
    private Kamer kamer;
    private int verblijfsKeuzeID;
    private VerblijfsKeuze verblijfsKeuze;

    public Prijs() {
    }
    public int getPrijsID() {
        return prijsID;
    }

    public void setPrijsID(int prijsID) {
        this.prijsID = prijsID;
    }

    public BigDecimal getPrijsPerKamer() {
        return prijsPerKamer;
    }

    public void setPrijsPerKamer(BigDecimal prijsPerKamer) {
        this.prijsPerKamer = prijsPerKamer;
    }

    public Date getDatumVanaf() {
        return datumVanaf;
    }

    public void setDatumVanaf(Date datumVanaf) {
        this.datumVanaf = datumVanaf;
    }

    public int getKamerID() {
        return kamerID;
    }

    public void setKamerID(int kamerID) {
        this.kamerID = kamerID;
    }

    public Kamer getKamer() {
        return kamer;
    }

    public void setKamer(Kamer kamer) {
        this.kamer = kamer;
    }

    public int getVerblijfsKeuzeID() {
        return verblijfsKeuzeID;
    }

    public void setVerblijfsKeuzeID(int verblijfsKeuzeID) {
        this.verblijfsKeuzeID = verblijfsKeuzeID;
    }

    public VerblijfsKeuze getVerblijfsKeuze() {
        return verblijfsKeuze;
    }

    public void setVerblijfsKeuze(VerblijfsKeuze verblijfsKeuze) {
        this.verblijfsKeuze = verblijfsKeuze;
    }
}
