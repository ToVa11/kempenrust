package pr4.t1.kempenrust.DTO;

import java.util.Date;

public class MeldingDto {
    private Date boekingDatumVan;
    private Date boekingDatumTot;
    private String foutmelding;
    private String melding;
    private String titel;
    private boolean isKlant;
    private boolean isKamer;


    public boolean isKamer() {
        return isKamer;
    }

    public void setKamer(boolean kamer) {
        isKamer = kamer;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getMelding() {
        return melding;
    }

    public void setMelding(String melding) {
        this.melding = melding;
    }

    public boolean isKlant() {
        return isKlant;
    }

    public void setKlant(boolean klant) {
        isKlant = klant;
    }

    public Date getBoekingDatumVan() {
        return boekingDatumVan;
    }

    public void setBoekingDatumVan(Date boekingDatumVan) {
        this.boekingDatumVan = boekingDatumVan;
    }

    public Date getBoekingDatumTot() {
        return boekingDatumTot;
    }

    public void setBoekingDatumTot(Date boekingDatumTot) {
        this.boekingDatumTot = boekingDatumTot;
    }

    public String getFoutmelding() {
        return foutmelding;
    }

    public void setFoutmelding(String foutmelding) {
        this.foutmelding = foutmelding;
    }
}
