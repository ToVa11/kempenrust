package pr4.t1.kempenrust.DTO;
import org.springframework.format.annotation.DateTimeFormat;
import pr4.t1.kempenrust.model.KamerOnbeschikbaar;
import pr4.t1.kempenrust.model.KamerType;

import java.util.ArrayList;
import java.util.Date;

public class KamerBeheer {
    private int kamerID;
    private int kamerOnbeschikbaarID;
    private int kamerNummer;
    private int kamerTypeID;
    private Boolean Beschikbaarheid;
    private Boolean Geboekt;

    public Boolean getGeboekt() {
        return Geboekt;
    }

    public void setGeboekt(Boolean geboekt) {
        Geboekt = geboekt;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datumVan;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datumTot;
    private String omschrijving;

    public int getKamerOnbeschikbaarID() {
        return kamerOnbeschikbaarID;
    }

    public void setKamerOnbeschikbaarID(int kamerOnbeschikbaarID) {
        this.kamerOnbeschikbaarID = kamerOnbeschikbaarID;
    }
    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    ArrayList <KamerOnbeschikbaar> lisjtkamers;

    public ArrayList<KamerOnbeschikbaar> getLisjtkamers() {
        return lisjtkamers;
    }

    public void setLisjtkamers(ArrayList<KamerOnbeschikbaar> lisjtkamers) {
        this.lisjtkamers = lisjtkamers;
    }

    public Boolean getBeschikbaarheid() {
        return Beschikbaarheid;
    }

    public void setBeschikbaarheid(Boolean beschikbaarheid) {
        Beschikbaarheid = beschikbaarheid;
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

    public void setDatumTot(Date daumTot) {
        this.datumTot = daumTot;
    }

    public ArrayList<KamerType> getKamerTypes() {
        return kamerTypes;
    }

    public void setKamerTypes(ArrayList<KamerType> kamerTypes) {
        this.kamerTypes = kamerTypes;
    }

    private ArrayList<KamerType> kamerTypes;

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

}
