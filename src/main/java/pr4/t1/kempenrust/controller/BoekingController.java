package pr4.t1.kempenrust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pr4.t1.kempenrust.DTO.KamerBeheer;
import pr4.t1.kempenrust.model.*;
import pr4.t1.kempenrust.model.DTO.OverzichtDto;
import pr4.t1.kempenrust.model.DTO.ReserveringBevestigingDto;
import pr4.t1.kempenrust.DTO.BoekingDetailDto;
import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.DTO.ReserveringDto;
import pr4.t1.kempenrust.repository.BoekingDetailRepository;
import pr4.t1.kempenrust.repository.KamerRepository;
import pr4.t1.kempenrust.repository.VerblijfsKeuzeRepository;

import pr4.t1.kempenrust.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;

import java.time.LocalDate;
import java.time.YearMonth;

import java.text.SimpleDateFormat;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    KamerOnbeschikbaarRepository kamerOnbeschikbaarRepository;

    SimpleDateFormat simpleFormatter = new SimpleDateFormat("dd/MM/yyyy");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Date datumAankomst;
    Date datumVertrek;

    BoekingDetail[][] overzicht;

    //    Hier komen alle methodes die iets te maken hebben met boekingen
    @RequestMapping("/reserveren")
    public String Reserveren(Model model, HttpServletRequest request,  RedirectAttributes redirectAttributes) {
        if(redirectAttributes.containsAttribute("message")) {
            model.addAttribute(redirectAttributes.getAttribute("message"));
        }
        var datum = request.getParameter("Datum");
        var kamerId = request.getParameter("KamerID");

        ReserveringDto reserveringDto = new ReserveringDto();
        reserveringDto.setVerblijfsKeuzes(verblijfsKeuzeRepository.getAlleVerblijfsKeuzes());

        if(datum != null && kamerId != null) {
            reserveringDto.setDatumAankomst(datum);
            reserveringDto.setDatumVertrek(datum);
            reserveringDto.setAantalPersonen(1);
            List<Integer> kamers = Arrays.asList(Integer.parseInt(kamerId));
            reserveringDto.setKamers(kamers);
        }

        model.addAttribute("reserveringDetails", reserveringDto);

        return "layouts/boeking/reserveren";
    }

    // via params kan ik meerdere submits in mijn form gebruiken
    @RequestMapping(value = "/reserveren", method = RequestMethod.POST, params = "action=zoek-kamers")
    public String ZoekKamers(@ModelAttribute("reserveringDetails") ReserveringDto reserveringDetails, Model model, RedirectAttributes redirectAttributes) {
        String message = null;
        message = checkDatums(reserveringDetails.getDatumAankomst(), reserveringDetails.getDatumVertrek());

        if(message!=null) {
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:/reserveren";
        }

        vulDatumsOp(reserveringDetails.getDatumAankomst(), reserveringDetails.getDatumVertrek());

        reserveringDetails.setPrijsVrijeKamers(kamerRepository.getAlleVrijeKamers(reserveringDetails.getKeuzeArrangement(), datumAankomst, datumVertrek));

        if(reserveringDetails.getPrijsVrijeKamers().isEmpty())
        {
            message="Er is voor deze periode geen kamer meer beschikbaar.";
            redirectAttributes.addFlashAttribute("message",message);

            return "redirect:/reserveren";
        }

        //Hier ga ik nog eens verblijfskeuzes ophalen, hebben jullie een betere oplossing?
        //object Dto kan geen complexe objecten doorsturen (enkel int, double, String & List (van de afgelopen 3)
        reserveringDetails.setVerblijfsKeuzes(verblijfsKeuzeRepository.getAlleVerblijfsKeuzes());

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

        // Bij nieuwe reservering wordt er blijkbaar ergens een NULL bijgezet, maar ik vind niet waar
        // Deze code verwijderd deze NULL
        while(reserveringDetails.getKamers().remove(null)) {};

        int BoekingID = boekingRepository.toevoegenReservatie(
                klant.getKlantID(),
                reserveringDetails.getKeuzeArrangement(),
                bedragVoorschot,
                reserveringDetails.getAantalPersonen(),
                datumAankomst,
                datumVertrek,
                reserveringDetails.getKamers());

        ReserveringBevestigingDto bevestiging = new ReserveringBevestigingDto();

        var boeking = boekingRepository.getReservatieByID(BoekingID);

        bevestiging.setBoeking(boeking);
        bevestiging.setPrijzenKamers(prijsVoorBoeking);
        bevestiging.setTotaalPrijs(totaalPrijs);

        model.addAttribute("bevestiging", bevestiging);

        return "layouts/boeking/bevestiging";
}


    @RequestMapping("/reserveringen")
    public String Reserveringen(Model model, RedirectAttributes redirectAttributes) {

        //If redirectAttribute is set, this has to be added to the model, so we can show it at frontend
        if(redirectAttributes.containsAttribute("message")) {
            model.addAttribute(redirectAttributes.getAttribute("message"));
        }

        ArrayList<BoekingDetail> details = boekingDetailRepository.getAlleToekomstigeBoekingdetails();

        model.addAttribute("details",details);

        return "layouts/boeking/reserveringen";
    }

    @RequestMapping("/overzicht")
    public String Overzicht(Model model, Integer maand, Integer jaar) {
        OverzichtDto overzichtDto = new OverzichtDto();

        if(maand == null || jaar == null) {
            var vandaag = LocalDate.now();
            maand = vandaag.getMonth().getValue();
            jaar = vandaag.getYear();
        }

        overzichtDto.setMaand(maand);
        overzichtDto.setJaar(jaar);
        overzichtDto.setDatum(LocalDate.of(jaar, maand, 1));

        ArrayList<Kamer> kamers = kamerRepository.getAlleKamersMetModel();
        overzichtDto.setKamers(kamers);

        int dagenInMaand = YearMonth.of(jaar, maand).lengthOfMonth();
        overzichtDto.setDagenInMaand(dagenInMaand);

        Date van = Date.valueOf(LocalDate.of(jaar, maand, 1));
        Date tot = Date.valueOf(LocalDate.of(jaar, maand, dagenInMaand));

        overzicht = new BoekingDetail[kamers.size()][dagenInMaand];

        var lijstKamersOnbeschikbaar = kamerOnbeschikbaarRepository.getOnbeschikbaarKamerTussenTweeDatums(van, tot);
        VulOverzichtOp(lijstKamersOnbeschikbaar, kamers, maand, jaar);

        var boekingen = boekingDetailRepository.getBoekingsdetailsTussenTweeDatums(van, tot);
        VulOverzichtOp(boekingen, kamers, maand, jaar);

        overzichtDto.setOverzicht(overzicht);
        model.addAttribute("overzichtDto", overzichtDto);

        return "layouts/boeking/overzicht";
    }

    @RequestMapping("/voorschotten")
    public String Voorschotten(Model model, RedirectAttributes redirectAttributes) {
        List<Boeking> boekingenMetOnbetaaldeVoorschotten = boekingRepository.getBoekingenMetOnbetaaldVoorschot();

        if(redirectAttributes.containsAttribute("message")) {
            model.addAttribute(redirectAttributes.getAttribute("message"));
        }
        model.addAttribute("boekingen", boekingenMetOnbetaaldeVoorschotten);

        return "layouts/boeking/voorschotten";
    }

    @RequestMapping("/reservering/bevestig/voorschot")
    public String bevestigVoorschot(HttpServletRequest request, RedirectAttributes redirectAttributes){
        int rowsUpdated = boekingRepository.bevestigVoorschot(request.getParameter("boekingID"));
        String message=null;

        if(rowsUpdated> 0 ) {
            message="De betaling van het voorschot is bevestigd.";
        }
        else {
            message="Er is iets misgegaan bij het bevestigen van het voorschot.";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/voorschotten";


    }


    @RequestMapping("/afgelopen_reservaties")
    public String AfgelopenReserveringen(Model model) {
        ArrayList<BoekingDetail> details = boekingDetailRepository.getAfgelopenReservaties ();
        BoekingDetailDto boekingDetailDto=new BoekingDetailDto();
        boekingDetailDto.setTitel("Afgelopen Reservaties");
        model.addAttribute("details",details);
        model.addAttribute("boekingDetailDto",boekingDetailDto);

        return "layouts/boeking/afgelopen_reservaties";
    }

    @PostMapping("/afgelopen_reservaties")
    public String AfgelopenReserveringen(Model model,@ModelAttribute("KamerBeheer") BoekingDetailDto boekingDetailDto) {
        var datumVan = Date.valueOf(boekingDetailDto.getDatumVan());
        var datumTot = Date.valueOf(boekingDetailDto.getDatumTot());
        if (datumVan !=null && datumTot!=null && datumVan.before(datumTot))
        {
            ArrayList<BoekingDetailDto> details = boekingDetailRepository
            .getAlleDetailsMetDatums(datumVan,datumTot);

            String startDatum = simpleFormatter.format(datumVan);
            String endDatum = simpleFormatter.format(datumTot);

            if (details.size()>0){
                boekingDetailDto.setTitel("Reservaties tussen "+startDatum +" en "+endDatum);
            }else
            {
                boekingDetailDto.setTitel("Geen reservaties gevonden tussen "+startDatum+" en "+endDatum);
            }

            model.addAttribute("details",details);
            model.addAttribute("boekingDetailDto",boekingDetailDto);
            return "layouts/boeking/afgelopen_reservaties";
        }
        else
            boekingDetailDto.setTitel("Gelieve de datums te controleren");
            model.addAttribute("boekingDetailDto", boekingDetailDto);
            return "layouts/boeking/afgelopen_reservaties";

    }

    private Klant getKlantVoorBevestigingReservering(ReserveringDto reserveringDetails) {
        if(klantRepository.customerExists(reserveringDetails.getEmail()) == false) {
            klantRepository.toevoegenKlant(
                    reserveringDetails.getVoornaam(),
                    reserveringDetails.getNaam(),
                    reserveringDetails.getTelefoon(),
                    reserveringDetails.getEmail());
        }
        return klantRepository.getKlantByEmail(reserveringDetails.getEmail());
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

    private <T> void VulOverzichtOp(ArrayList<T> lijst, ArrayList<Kamer> kamers, int maand, int jaar) {
        LocalDate datumVan;
        LocalDate datumTot;
        for (var item: lijst) {
            if (item instanceof KamerOnbeschikbaar) {
                datumVan = new java.sql.Date(((KamerOnbeschikbaar) item).getDatumVan().getTime()).toLocalDate();
                datumTot = new java.sql.Date(((KamerOnbeschikbaar) item).getDatumTot().getTime()).toLocalDate();
            } else {
                datumVan = new java.sql.Date(((BoekingDetail) item).getBoeking().getDatumVan().getTime()).toLocalDate();
                datumTot = new java.sql.Date(((BoekingDetail) item).getBoeking().getDatumTot().getTime()).toLocalDate();
            }
            for (LocalDate date = datumVan; date.isBefore(datumTot.plusDays(1)); date = date.plusDays(1)) {
                if(date.getMonth().getValue() == maand && date.getYear() == jaar) {
                    int column = date.getDayOfMonth() - 1;
                    if (item instanceof KamerOnbeschikbaar) {
                        overzicht[kamers.indexOf(((KamerOnbeschikbaar) item).getKamer())][column] = new BoekingDetail();
                    } else {
                        overzicht[kamers.indexOf(((BoekingDetail) item).getKamer())][column] = ((BoekingDetail) item);
                    }
                }
            }
        }
    }

    private String checkDatums(String datumAankomst, String datumVertrek) {
        LocalDate aankomst = LocalDate.parse(datumAankomst,formatter);
        LocalDate vertrek = LocalDate.parse(datumVertrek, formatter);
        String message=null;

        if(aankomst.isBefore(LocalDate.now()) || vertrek.isBefore(LocalDate.now()) ) {
            message = "Gelieve een datum in de toekomst te kiezen.";
        }
        else if(vertrek.isBefore(aankomst)) {
            message = "De vertrekdatum kan niet voor de aankomstdatum liggen.";
        }

        return message;
    }
}
