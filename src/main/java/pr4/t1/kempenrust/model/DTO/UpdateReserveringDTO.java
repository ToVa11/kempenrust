package pr4.t1.kempenrust.model.DTO;

import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.Klant;
import pr4.t1.kempenrust.model.Prijs;
import pr4.t1.kempenrust.model.VerblijfsKeuze;

import java.util.List;

public class UpdateReserveringDTO {

    private Klant klant;
    private String datumVan;
    private String datumTot;
    private int verblijfskeuzeID;
    private int aantalPersonen;
    private int boekingID;
    private List<VerblijfsKeuze> verblijfsKeuzes;
    private List<Prijs> prijsKamers;
    private List<Prijs> prijsKamersBoeking;
    private List<Integer> kamers;
    private List<Integer> geboekteKamers;

    public Klant getKlant() {
        return klant;
    }

    public void setKlant(Klant klant) {
        this.klant = klant;
    }
    public int getBoekingID() {
        return boekingID;
    }

    public void setBoekingID(int boekingID) {
        this.boekingID = boekingID;
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

    public int getVerblijfskeuzeID() {
        return verblijfskeuzeID;
    }

    public void setVerblijfskeuzeID(int verblijfskeuzeID) {
        this.verblijfskeuzeID = verblijfskeuzeID;
    }

    public int getAantalPersonen() {
        return aantalPersonen;
    }

    public void setAantalPersonen(int aantalPersonen) {
        this.aantalPersonen = aantalPersonen;
    }

    public List<VerblijfsKeuze> getVerblijfsKeuzes() {
        return verblijfsKeuzes;
    }

    public void setVerblijfsKeuzes(List<VerblijfsKeuze> verblijfsKeuzes) {
        this.verblijfsKeuzes = verblijfsKeuzes;
    }

    public List<Prijs> getPrijsKamers() {
        return prijsKamers;
    }

    public void setPrijsKamers(List<Prijs> prijsKamers) {
        this.prijsKamers = prijsKamers;
    }

    public List<Prijs> getPrijsKamersBoeking() {
        return prijsKamersBoeking;
    }

    public void setPrijsKamersBoeking(List<Prijs> prijsKamersBoeking) {
        this.prijsKamersBoeking = prijsKamersBoeking;
    }

    public List<Integer> getKamers() {
        return kamers;
    }

    public void setKamers(List<Integer> kamers) {
        this.kamers = kamers;
    }

    public List<Integer> getGeboekteKamers() {
        return geboekteKamers;
    }

    public void setGeboekteKamers(List<Integer> geboekteKamers) {
        this.geboekteKamers = geboekteKamers;
    }
}
