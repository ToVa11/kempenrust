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
    private Boolean isBeschikbaarheid;
    private Boolean isGeboekt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datumVan;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datumTot;
    private String omschrijving;
    private ArrayList<KamerType> kamerTypes;
    private Boolean IsInPrijs;
    private ArrayList <KamerOnbeschikbaar> lijstkamers;


    public Boolean getInPrijs() {
        return IsInPrijs;
    }

    public void setInPrijs(Boolean inPrijs) {
        IsInPrijs = inPrijs;
    }

    public Boolean getBeschikbaarheid() {
        return isBeschikbaarheid;
    }

    public void setBeschikbaarheid(Boolean beschikbaarheid) {
        isBeschikbaarheid = beschikbaarheid;
    }

    public Boolean getGeboekt() {
        return isGeboekt;
    }

    public void setGeboekt(Boolean geboekt) {
        isGeboekt = geboekt;
    }

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

    public ArrayList<KamerOnbeschikbaar> getLijstkamers() {
        return lijstkamers;
    }

    public void setLijstkamers(ArrayList<KamerOnbeschikbaar> lijstkamers) {
        this.lijstkamers = lijstkamers;
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

    public ArrayList<KamerType> getKamerTypes() {
        return kamerTypes;
    }

    public void setKamerTypes(ArrayList<KamerType> kamerTypes) {
        this.kamerTypes = kamerTypes;
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

}
