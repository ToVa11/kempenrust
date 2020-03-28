package pr4.t1.kempenrust.model;

import java.util.Date;

public class KamerOnbeschikbaar {
    private int kamerOnbeschikbaarID;
    private Date datumVan;
    private Date datumTot;
    private int kamerID;
    private Kamer kamer;

    public KamerOnbeschikbaar() {
    }

    public int getKamerOnbeschikbaarID() {
        return kamerOnbeschikbaarID;
    }

    public void setKamerOnbeschikbaarID(int kamerOnbeschikbaarID) {
        this.kamerOnbeschikbaarID = kamerOnbeschikbaarID;
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
