package pr4.t1.kempenrust.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kamer kamer = (Kamer) o;
        return getKamerID() == kamer.getKamerID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKamerID());
    }
}

