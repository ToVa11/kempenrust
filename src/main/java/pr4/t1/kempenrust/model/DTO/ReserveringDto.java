package pr4.t1.kempenrust.model.DTO;

import pr4.t1.kempenrust.model.Prijs;
import pr4.t1.kempenrust.model.VerblijfsKeuze;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ReserveringDto {

    private String datumAankomst;
    private String datumVertrek;
    private int aantalPersonen;
    private List<VerblijfsKeuze> verblijfsKeuzes;
    private int keuzeArrangement;
    private String voornaam;
    private String naam;
    private String email;
    private String telefoon;
    private List<Prijs> prijsVrijeKamers;
    private List<Prijs> prijsKamers;

    public ReserveringDto() {
    }

    public String getDatumAankomst() {
        return datumAankomst;
    }

    public void setDatumAankomst(String datumAankomst) {
        this.datumAankomst = datumAankomst;
    }

    public String getDatumVertrek() {
        return datumVertrek;
    }

    public void setDatumVertrek(String datumVertrek) {
        this.datumVertrek = datumVertrek;
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

    public int getKeuzeArrangement() {
        return keuzeArrangement;
    }

    public void setKeuzeArrangement(int keuzeArrangement) {
        this.keuzeArrangement = keuzeArrangement;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefoon() {
        return telefoon;
    }

    public void setTelefoon(String telefoon) {
        this.telefoon = telefoon;
    }

    public List<Prijs> getPrijsVrijeKamers() {
        return prijsVrijeKamers;
    }

    public void setPrijsVrijeKamers(List<Prijs> prijsVrijeKamers) {
        this.prijsVrijeKamers = prijsVrijeKamers;
    }

    public List<Prijs> getPrijsKamers() {
        return prijsKamers;
    }

    public void setPrijsKamers(List<Prijs> prijsKamers) {
        this.prijsKamers = prijsKamers;
    }
}
