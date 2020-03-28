package pr4.t1.kempenrust.model;

public class VerblijfsKeuze {
    private int verblijfsKeuzID;
    private String naam;
    private String omschrijving;

    public VerblijfsKeuze() {
    }

    public int getVerblijfsKeuzID() {
        return verblijfsKeuzID;
    }

    public void setVerblijfsKeuzID(int verblijfsKeuzID) {
        this.verblijfsKeuzID = verblijfsKeuzID;
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
