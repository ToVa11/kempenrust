package pr4.t1.kempenrust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pr4.t1.kempenrust.model.*;
import pr4.t1.kempenrust.model.DTO.ReserveringBevestigingDto;
import pr4.t1.kempenrust.model.DTO.ReserveringDto;
import pr4.t1.kempenrust.repository.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BoekingController {
    @Autowired
    BoekingRepository boekingRepository;
    @Autowired
    BoekingDetailRepository boekingDetailRepository;
    @Autowired
    VerblijfsKeuzeRepository verblijfsKeuzeRepository;
    @Autowired
    KamerRepository kamerRepository;
    @Autowired
    KlantRepository klantRepository;
    @Autowired
    PrijsRepository prijsRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Date datumAankomst;
    Date datumVertrek;

    //    Hier komen alle methodes die iets te maken hebben met boekingen
    @RequestMapping("/reserveren")
    public String Reserveren(Model model) {

        ReserveringDto reserveringDto = new ReserveringDto();
        reserveringDto.setVerblijfsKeuzes(verblijfsKeuzeRepository.getAllVerblijfsKeuzes());

        model.addAttribute("reserveringDetails", reserveringDto);

        return "layouts/boeking/reserveren";
    }

    // via params kan ik meerdere submits in mijn form gebruiken
    @RequestMapping(value = "/reserveren", method = RequestMethod.POST, params = "action=zoek-kamers")
    public String ZoekKamers(@ModelAttribute("reserveringDetails") ReserveringDto reserveringDetails, Model model) {
        vulDatumsOp(reserveringDetails.getDatumAankomst(), reserveringDetails.getDatumVertrek());

        reserveringDetails.setPrijsVrijeKamers(kamerRepository.getAllAvailableRooms(reserveringDetails.getKeuzeArrangement(), datumAankomst, datumVertrek));

        //Hier ga ik nog eens verblijfskeuzes ophalen, hebben jullie een betere oplossing?
        //object Dto kan geen complexe objecten doorsturen (enkel int, double, String & List (van de afgelopen 3)
        reserveringDetails.setVerblijfsKeuzes(verblijfsKeuzeRepository.getAllVerblijfsKeuzes());

        model.addAttribute("reserveringDetails", reserveringDetails);

        return "layouts/boeking/reserveren";
    }

    // via params kan ik meerdere submits in mijn form gebruiken
    @PostMapping(value = "/reserveren", params = "action=bevestigen")
    public String ReserveringBevestigen(@ModelAttribute("reserveringDetails") ReserveringDto reserveringDetails,  Model model) {
        // Is enkel nodig als de page word gerefreshed, omdat ik hier met classvariables werk
        vulDatumsOp(reserveringDetails.getDatumAankomst(), reserveringDetails.getDatumVertrek());

        Klant klant = getKlantVoorBevestigingReservering(reserveringDetails);

        var prijsVoorBoeking = prijsRepository.GetPrijzenVoorReservatie(
                reserveringDetails.getKeuzeArrangement(),
                reserveringDetails.getKamers());

        BigDecimal totaalPrijs = getTotalePrijsVoorBoeking(prijsVoorBoeking);

        BigDecimal bedragVoorschot = getBedragVoorschot(totaalPrijs);

        int BoekingID = boekingRepository.createReservation(
                klant.getKlantID(),
                reserveringDetails.getKeuzeArrangement(),
                bedragVoorschot,
                reserveringDetails.getAantalPersonen(),
                datumAankomst,
                datumVertrek,
                reserveringDetails.getKamers());

        ReserveringBevestigingDto bevestiging = new ReserveringBevestigingDto();

        var boeking = boekingRepository.getReservationByID(BoekingID);

        bevestiging.setBoeking(boeking);
        bevestiging.setPrijzenKamers(prijsVoorBoeking);
        bevestiging.setTotaalPrijs(totaalPrijs);

        model.addAttribute("bevestiging", bevestiging);

        return "layouts/boeking/bevestiging";
}

    @RequestMapping("/reserveringen")
    public String Reserveringen(Model model) {

        ArrayList<BoekingDetail> details = boekingDetailRepository.getAllFutureDetails();

        model.addAttribute("details",details);

        return "layouts/boeking/reserveringen";
    }

    @RequestMapping("/overzicht")
    public String Overzicht() {
        return "layouts/boeking/overzicht";
    }

    @RequestMapping("/voorschotten")
    public String Voorschotten() {
        return "layouts/boeking/voorschotten";
    }

    @RequestMapping("/afgelopen_reservaties")
    public String AfgelopenReserveringen() {
        return "layouts/boeking/afgelopen_reservaties";
    }

    private Klant getKlantVoorBevestigingReservering(ReserveringDto reserveringDetails) {
        if(klantRepository.customerExists(reserveringDetails.getEmail()) == false) {
            klantRepository.createCustomer(
                    reserveringDetails.getVoornaam(),
                    reserveringDetails.getNaam(),
                    reserveringDetails.getTelefoon(),
                    reserveringDetails.getEmail());
        }
        return klantRepository.getCustomerByEmail(reserveringDetails.getEmail());
    }

    private BigDecimal getTotalePrijsVoorBoeking(ArrayList<Prijs> prijzenReservatie) {
        BigDecimal totaalPrijs = new BigDecimal(0);

        for (Prijs prijs: prijzenReservatie) {
            totaalPrijs = totaalPrijs.add(prijs.getPrijsPerKamer());
        }
        long diffDates = getVerschilDatums(datumAankomst, datumVertrek);

        return totaalPrijs.multiply(new BigDecimal(diffDates));
    }

    private BigDecimal getBedragVoorschot(BigDecimal totaalPrijs) {
        BigDecimal bedragVoorschot = new BigDecimal(0);
        BigDecimal voorschotPercentage = new BigDecimal(10);
        int dagenGeenVoorschot = 7;

        var today = new Date(new java.util.Date().getTime());
        long diffDates =  getVerschilDatums(today, datumAankomst);

        if(diffDates > dagenGeenVoorschot)
            bedragVoorschot = totaalPrijs.divide(voorschotPercentage);

        return bedragVoorschot;
    }

    private void vulDatumsOp(String datumVan, String datumTot) {
        datumAankomst = Date.valueOf(datumVan);
        datumVertrek = (datumTot != "")
                ? Date.valueOf(datumTot)
                : null;
    }

    private long getVerschilDatums(Date datumVan, Date datumTot) {
        return (datumTot.getTime() - datumVan.getTime()) / (24 * 60 * 60 * 1000);
    }
}
