package pr4.t1.kempenrust.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

public class BoekingDetail {
    private int boekingDetailID;
    private int boekingID;
    private Boeking boeking;
    private int kamerID;
    private Kamer kamer;

    public BoekingDetail() {
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
