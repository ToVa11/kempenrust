package pr4.t1.kempenrust.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pr4.t1.kempenrust.model.*;
import pr4.t1.kempenrust.model.DTO.*;
import pr4.t1.kempenrust.model.BoekingDetail;
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
    //region DI Repos
    @Autowired
    private BoekingRepository boekingRepository;
    @Autowired
    private BoekingDetailRepository boekingDetailRepository;
    @Autowired
    private VerblijfsKeuzeRepository verblijfsKeuzeRepository;
    @Autowired
    private KamerRepository kamerRepository;
    @Autowired
    private KlantRepository klantRepository;
    @Autowired
    private PrijsRepository prijsRepository;
    @Autowired
    private KamerOnbeschikbaarRepository kamerOnbeschikbaarRepository;
    //endregion

    //region Class variables
    private SimpleDateFormat simpleFormatter = new SimpleDateFormat("dd/MM/yyyy");

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Date datumAankomst;
    private Date datumVertrek;

    private BoekingDetail[][] overzicht;
    //endregion

    //region Boeken
    @RequestMapping("/reserveren")
    public String getBoekingForm(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if(redirectAttributes.containsAttribute("message")) {
            model.addAttribute(redirectAttributes.getAttribute("message"));
        }
        var datum = request.getParameter("Datum");
        var kamerId = request.getParameter("KamerID");

        ReserveringDto reserveringDto = new ReserveringDto();
        reserveringDto.setVerblijfsKeuzes(verblijfsKeuzeRepository.get());

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

    @RequestMapping("/zoekkamers")
    public String getBeschikbareKamers(@ModelAttribute("reserveringDetails") ReserveringDto reserveringDetails, Model model, RedirectAttributes redirectAttributes) {
        String message = null;
        message = checkDatums(reserveringDetails.getDatumAankomst(), reserveringDetails.getDatumVertrek());

        if(message!=null) {
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:/reserveren";
        }

        vulDatumsOp(reserveringDetails.getDatumAankomst(), reserveringDetails.getDatumVertrek());

        reserveringDetails.setPrijsVrijeKamers(kamerRepository.getByBeschikbaarheid(reserveringDetails.getKeuzeArrangement(), datumAankomst, datumVertrek));

        if(reserveringDetails.getPrijsVrijeKamers().isEmpty())
        {
            message="Er is voor deze periode geen kamer meer beschikbaar.";
            redirectAttributes.addFlashAttribute("message",message);

            return "redirect:/reserveren";
        }

        //Hier ga ik nog eens verblijfskeuzes ophalen, hebben jullie een betere oplossing?
        //object Dto kan geen complexe objecten doorsturen (enkel int, double, String & List (van de afgelopen 3)
        reserveringDetails.setVerblijfsKeuzes(verblijfsKeuzeRepository.get());

        model.addAttribute("reserveringDetails", reserveringDetails);

        return "layouts/boeking/reserveren";
    }

    @PostMapping("/reserveren")
    public String createBoeking(@ModelAttribute("reserveringDetails") ReserveringDto reserveringDetails, Model model) {

        // Is enkel nodig als de page word gerefreshed, omdat ik hier met classvariables werk
        vulDatumsOp(reserveringDetails.getDatumAankomst(), reserveringDetails.getDatumVertrek());

        Klant klant = getKlantVoorBevestigingReservering(reserveringDetails);

        var prijsVoorBoeking = prijsRepository.getByKamers(
                reserveringDetails.getKeuzeArrangement(),
                reserveringDetails.getKamers());

        BigDecimal totaalPrijs = getTotalePrijsVoorBoeking(prijsVoorBoeking);

        BigDecimal bedragVoorschot = getBedragVoorschot(totaalPrijs);

        // Bij nieuwe reservering wordt er blijkbaar ergens een NULL bijgezet, maar ik vind niet waar
        // Deze code verwijderd deze NULL
        while(reserveringDetails.getKamers().remove(null)) {};

        int BoekingID = boekingRepository.create(
                klant.getKlantID(),
                reserveringDetails.getKeuzeArrangement(),
                bedragVoorschot,
                reserveringDetails.getAantalPersonen(),
                datumAankomst,
                datumVertrek,
                reserveringDetails.getKamers());

        ReserveringBevestigingDto bevestiging = new ReserveringBevestigingDto();

        var boeking = boekingRepository.getById(BoekingID);

        bevestiging.setBoeking(boeking);
        bevestiging.setPrijzenKamers(prijsVoorBoeking);
        bevestiging.setTotaalPrijs(totaalPrijs);

        model.addAttribute("bevestiging", bevestiging);

        return "layouts/boeking/bevestiging";
}
    //endregion

    //region Overzicht & Lijsten
    @RequestMapping("/reserveringen")
    public String getBoekingen(Model model, RedirectAttributes redirectAttributes) {

        //If redirectAttribute is set, this has to be added to the model, so we can show it at frontend
        if(redirectAttributes.containsAttribute("message")) {
            model.addAttribute(redirectAttributes.getAttribute("message"));
        }

        ArrayList<BoekingDetail> details = boekingDetailRepository.getToekomst();

        model.addAttribute("details",details);

        return "layouts/boeking/reserveringen";
    }

    @RequestMapping("/overzicht")
    public String getOverzicht(Model model, Integer maand, Integer jaar) {
        OverzichtDto overzichtDto = new OverzichtDto();

        if(maand == null || jaar == null) {
            var vandaag = LocalDate.now();
            maand = vandaag.getMonth().getValue();
            jaar = vandaag.getYear();
        }

        overzichtDto.setMaand(maand);
        overzichtDto.setJaar(jaar);
        overzichtDto.setDatum(LocalDate.of(jaar, maand, 1));

        ArrayList<Kamer> kamers = kamerRepository.get();
        overzichtDto.setKamers(kamers);

        int dagenInMaand = YearMonth.of(jaar, maand).lengthOfMonth();
        overzichtDto.setDagenInMaand(dagenInMaand);

        Date van = Date.valueOf(LocalDate.of(jaar, maand, 1));
        Date tot = Date.valueOf(LocalDate.of(jaar, maand, dagenInMaand));

        overzicht = new BoekingDetail[kamers.size()][dagenInMaand];

        var lijstKamersOnbeschikbaar = kamerOnbeschikbaarRepository.getTussenTweeDatums(van, tot);
        VulOverzichtOp(lijstKamersOnbeschikbaar, kamers, maand, jaar);

        var boekingen = boekingDetailRepository.getTussenTweeDatums(van, tot);
        VulOverzichtOp(boekingen, kamers, maand, jaar);

        overzichtDto.setOverzicht(overzicht);
        model.addAttribute("overzichtDto", overzichtDto);

        return "layouts/boeking/overzicht";
    }

    @RequestMapping("/afgelopen_reservaties")
    public String getAfgelopenBoekingen(Model model) {
        BoekingDetailDto boeking=new BoekingDetailDto();
        ArrayList<BoekingDetail> details = boekingDetailRepository.getVerleden();
        MeldingDto melding=new MeldingDto();
        Date datumGisteren= Date.valueOf(LocalDate.now().minusDays(1));
        boeking.setDatumTot(datumGisteren.toString());
        melding.setTitel("Afgelopen Reservaties");
        model.addAttribute("details",details);
        model.addAttribute("boeking",boeking);
        model.addAttribute("melding",melding);
        return "layouts/boeking/afgelopen_reservaties";
    }

    @PostMapping("/afgelopen_reservaties")
    public String getAfgelopenBoekingen(Model model, @ModelAttribute("Boeking") BoekingDetailDto boeking) {
        MeldingDto melding=new MeldingDto();
        var datumVan = Date.valueOf(boeking.getDatumVan());
        var datumTot = Date.valueOf(LocalDate.now().minusDays(1));
        if (datumVan !=null && datumTot!=null && datumVan.before(datumTot))
        {
            ArrayList<BoekingDetail> details = boekingDetailRepository
                    .getTussenTweeDatums(datumVan,datumTot);

            String startDatum = simpleFormatter.format(datumVan);
            String endDatum = simpleFormatter.format(datumTot);

            if (details.size()>0){
                melding.setTitel("Reservaties tussen "+startDatum +" en "+endDatum);
            }else
            {
                melding.setTitel("Geen reservaties gevonden tussen "+startDatum+" en "+endDatum);
            }
            Date datumGisteren= Date.valueOf(LocalDate.now().minusDays(1));
            boeking.setDatumTot(datumGisteren.toString());
            model.addAttribute("details",details);
            model.addAttribute("melding",melding);
            model.addAttribute("boeking",boeking);
            return "layouts/boeking/afgelopen_reservaties";
        }
        else {
            melding.setTitel("Geen reservaties gevonden");
            melding.setFoutmelding("Gelieve de datums te controleren");
            model.addAttribute("melding", melding);
            model.addAttribute("boeking", boeking);
            return "layouts/boeking/afgelopen_reservaties";
        }
    }
    //endregion

    //region Voorschotten
    @RequestMapping("/voorschotten")
    public String getVoorschotten(Model model, RedirectAttributes redirectAttributes) {
        List<Boeking> boekingenMetOnbetaaldeVoorschotten = boekingRepository.getByOnbetaaldeVoorschotten();

        if(redirectAttributes.containsAttribute("message")) {
            model.addAttribute(redirectAttributes.getAttribute("message"));
        }
        model.addAttribute("boekingen", boekingenMetOnbetaaldeVoorschotten);

        return "layouts/boeking/voorschotten";
    }

    @RequestMapping("/reservering/bevestig/voorschot")
    public String updateVoorschotBetaald(HttpServletRequest request, RedirectAttributes redirectAttributes){
        int rowsUpdated = boekingRepository.updateVoorschotBetaald(request.getParameter("boekingID"));
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
    //endregion

    //region Private methods
    private Klant getKlantVoorBevestigingReservering(ReserveringDto reserveringDetails) {
        if(klantRepository.existsByEmail(reserveringDetails.getEmail()) == false) {
            klantRepository.create(
                    reserveringDetails.getVoornaam(),
                    reserveringDetails.getNaam(),
                    reserveringDetails.getTelefoon(),
                    reserveringDetails.getEmail());
        }
        return klantRepository.getByEmail(reserveringDetails.getEmail());
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
    //endregion
}
