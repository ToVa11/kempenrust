package pr4.t1.kempenrust.model.DTO;

import org.springframework.format.annotation.DateTimeFormat;
import pr4.t1.kempenrust.model.KamerType;

import java.util.ArrayList;
import java.util.Date;

public  class KamerDto {
    private int kamerID;
    private int kamerNummer;
    private int kamerTypeID;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datumVan;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datumTot;
    private String omschrijving;
    private ArrayList<KamerType> kamerTypes;
    private Boolean isWijziging;

    public Boolean getWijziging() {
        return isWijziging;
    }

    public void setWijziging(Boolean wijziging) {
        isWijziging = wijziging;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
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

    public ArrayList<KamerType> getKamerTypes() {
        return kamerTypes;
    }

    public void setKamerTypes(ArrayList<KamerType> kamerTypes) {
        this.kamerTypes = kamerTypes;
    }

}