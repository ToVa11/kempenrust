package pr4.t1.kempenrust.model.DTO;

import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.Klant;
import pr4.t1.kempenrust.model.VerblijfsKeuze;

import java.util.List;

public class UpdateReserveringDTO {

    private Klant klant;
    private List<VerblijfsKeuze> verblijfsKeuzes;
    private String datumVan;
    private String datumTot;
    private int verblijfskeuzeID;
    private int aantalPersonen;
    private int boekingID;

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
}
