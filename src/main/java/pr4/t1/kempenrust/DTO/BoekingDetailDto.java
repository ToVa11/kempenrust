package pr4.t1.kempenrust.DTO;

import org.springframework.format.annotation.DateTimeFormat;
import pr4.t1.kempenrust.model.Boeking;
import pr4.t1.kempenrust.model.Kamer;

import java.util.Date;

public class BoekingDetailDto {
    private int boekingDetailID;
    private int boekingID;
    private Boeking boeking;
    private int kamerID;
    private Kamer kamer;
    private String datumVan;
    private String datumTot;
    private String titel;

    public String getTitel() {
        return titel;
    }
    public void setTitel(String titel) {
        this.titel = titel;
    }
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

    public int getBoekingDetailID() {
        return boekingDetailID;
    }

    public int getBoekingDetailsID() {
        return boekingDetailID;
    }

    public void setBoekingDetailID(int boekingDetailID) {
        this.boekingDetailID = boekingDetailID;
    }

    public int getBoekingID() {
        return boekingID;
    }

    public void setBoekingID(int boekingID) {
        this.boekingID = boekingID;
    }

    public Boeking getBoeking() {
        return boeking;
    }

    public void setBoeking(Boeking boeking) {
        this.boeking = boeking;
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
}
