package pr4.t1.kempenrust.model;

public class VerblijfsKeuze {
    private int verblijfskeuzeID;
    private String naam;
    private String omschrijving;

    public VerblijfsKeuze() {
    }

    public int getVerblijfskeuzeID() {
        return verblijfskeuzeID;
    }

    public void setVerblijfskeuzeID(int verblijfsKeuzeID) {
        this.verblijfskeuzeID = verblijfskeuzeID;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }
}
