package pr4.t1.kempenrust.model;

import java.math.BigDecimal;
import java.util.Date;

public class Boeking {
    private int boekingID;
    private Date datum;
    private BigDecimal bedragVoorschot;
    private int aantalPersonen;
    private Date datumVan;
    private Date datumTot;
    private boolean isBetaald;
    private int klantID;
    private Klant klant;
    private int verblijfsKeuzeID;
    private VerblijfsKeuze verblijfsKeuze;

    public Boeking() {
    }

    public int getBoekingID() {
        return boekingID;
    }
    public VerblijfsKeuze getVerblijfsKeuze() {
        return verblijfsKeuze;
    }

    public void setVerblijfsKeuze(VerblijfsKeuze verblijfsKeuze) {
        this.verblijfsKeuze = verblijfsKeuze;
    }


    public void setBoekingID(int boekingID) {
        this.boekingID = boekingID;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public BigDecimal getBedragVoorschot() {
        return bedragVoorschot;
    }

    public void setBedragVoorschot(BigDecimal bedragVoorschot) {
        this.bedragVoorschot = bedragVoorschot;
    }

    public int getAantalPersonen() {
        return aantalPersonen;
    }

    public void setAantalPersonen(int aantalPersonen) {
        this.aantalPersonen = aantalPersonen;
    }

    public Date getDatumVan() {
        return datumVan;
    }

    public void setDatumVan(Date datumVan) {
        this.datumVan = datumVan;
    }

    public Date getDatumTot() {
        return datumTot;
    }

    public void setDatumTot(Date datumTot) {
        this.datumTot = datumTot;
    }

    public boolean isBetaald() {
        return isBetaald;
    }

    public void setBetaald(boolean betaald) {
        isBetaald = betaald;
    }

    public int getKlantID() {
        return klantID;
    }

    public void setKlantID(int klantID) {
        this.klantID = klantID;
    }

    public Klant getKlant() {
        return klant;
    }

    public void setKlant(Klant klant) {
        this.klant = klant;
    }

    public int getVerblijfsKeuzeID() {
        return verblijfsKeuzeID;
    }

    public void setVerblijfsKeuzeID(int verblijfsKeuzID) {
        this.verblijfsKeuzeID = verblijfsKeuzeID;
    }
}

