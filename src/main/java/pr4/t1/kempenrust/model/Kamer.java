package pr4.t1.kempenrust.model;

public class Kamer {
    private int kamerID;
    private int kamerNummer;
    private int kamerTypeID;
    private KamerType kamerType;

    public Kamer() {
    }

    public int getKamerID() {
        return kamerID;
    }

    public void setKamerID(int kamerID) {
        this.kamerID = kamerID;
    }

    public int getKamerNummer() {
        return kamerNummer;
    }

    public void setKamerNummer(int kamerNummer) {
        this.kamerNummer = kamerNummer;
    }

    public int getKamerTypeID() {
        return kamerTypeID;
    }

    public void setKamerTypeID(int kamerTypeID) {
        this.kamerTypeID = kamerTypeID;
    }

    public KamerType getKamerType() {
        return kamerType;
    }

    public void setKamerType(KamerType kamerType) {
        this.kamerType = kamerType;
    }
}

