package pr4.t1.kempenrust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pr4.t1.kempenrust.model.*;

import pr4.t1.kempenrust.model.DTO.KamerDto;
import pr4.t1.kempenrust.model.DTO.MeldingDto;

import pr4.t1.kempenrust.model.DTO.ArrangementDTO;

import pr4.t1.kempenrust.model.DTO.UpdateReserveringDTO;
import pr4.t1.kempenrust.repository.BoekingDetailRepository;
import pr4.t1.kempenrust.repository.BoekingRepository;
import pr4.t1.kempenrust.repository.KlantRepository;
import pr4.t1.kempenrust.repository.VerblijfsKeuzeRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import pr4.t1.kempenrust.repository.*;

import java.sql.Date;
import java.text.ParseException;



import java.math.BigDecimal;


import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BeherenController {

    @Autowired
    BoekingDetailRepository boekingDetailRepository;
    @Autowired
    VerblijfsKeuzeRepository verblijfsKeuzeRepository;
    @Autowired
    BoekingRepository boekingRepository;
    @Autowired
    KlantRepository klantRepository;

    //    Hier komen alle methodes in die iets te maken hebben met het beheren (CRUD) van het hotel
    @Autowired
    private KamerRepository kamerRepository;
    @Autowired
    private KamerTypeRepository kamerTypeRepository;
    @Autowired
    private KamerOnbeschikbaarRepository kamerOnbeschikbaarRepository;
    @Autowired
    private PrijsRepository prijsRepository;
    // Hier komen alle methodes in die iets te maken hebben met het beheren (CRUD) van het hotel

    @RequestMapping("/klanten")
    public String klanten(Model model) {
        MeldingDto melding= new MeldingDto();
        ArrayList<Klant> klanten=klantRepository.getKlanten();
        model.addAttribute("melding",melding);
        model.addAttribute("klanten",klanten);
        return "layouts/beheren/klanten";
    }

    @RequestMapping("/KlantgegevensAanpassen")
    public String klantAanpassen(Model model, HttpServletRequest request){
        int klantId= Integer.parseInt(request.getParameter("klantId"));
        Klant klant=klantRepository.getKlantById(klantId);
        model.addAttribute("klant",klant);
        return "layouts/beheren/klantgegevensAanpassen";
    }

    @PostMapping("/wijzigKlant")
    public String WijzigKlant( Model model,@ModelAttribute("klant") Klant klant){
        MeldingDto melding= new MeldingDto();
        klantRepository.wijzigKlant(klant.getKlantID(), klant.getVoornaam(),
                        klant.getNaam(), klant.getTelefoonnummer(),
                        klant.getEmail(),klant.getStraat(), klant.getHuisnummer(),
                        klant.getPostcode(),klant.getGemeente());

        ArrayList<Klant> klanten=klantRepository.getKlanten();
        model.addAttribute("klanten",klanten);
        model.addAttribute("melding",melding);
        return "layouts/beheren/klanten";
    }

    @RequestMapping("/klantVerwijderen")
    public String KlantVerwijderen(Model model, HttpServletRequest request){
        MeldingDto melding=new MeldingDto();
        int klantId= Integer.parseInt(request.getParameter("klantId"));
        Boeking boeking = boekingRepository.getByKlantId(klantId);
        if (boeking.getDatumVan() !=null && boeking.getDatumTot()!=null)
        {
            melding.setFoutmelding("Attentie! Voor deze klant bestaat er reeds een boeking:");
            melding.setBoekingDatumVan(boeking.getDatumVan());
            melding.setBoekingDatumTot(boeking.getDatumTot());
            melding.setKlant(true);
            model.addAttribute("melding",melding);
            return "layouts/beheren/boodschap";
        }
        klantRepository.klantVerwijderen(klantId);
        ArrayList<Klant> klanten=klantRepository.getKlanten();
        model.addAttribute("klanten",klanten);
        return "layouts/beheren/klanten";
    }

    @RequestMapping("/kamers")
    public String Kamers(Model model) {
        MeldingDto melding=new MeldingDto();
        ArrayList<KamerOnbeschikbaar> kamers=kamerRepository.getWithKamerOnbeschikbaar();
        model.addAttribute("kamers",kamers);
        model.addAttribute("melding",melding);
        return "layouts/beheren/kamers";
    }

    @RequestMapping("/kamerAanpassen")
    public String kamerAanpassen(Model model, HttpServletRequest request){
        int kamerId= Integer.parseInt(request.getParameter("kamerId"));
        Kamer kamer=kamerRepository.getById(kamerId);
        ArrayList<KamerType> kamerTypes=kamerTypeRepository.getLijstKamerTypes();
        model.addAttribute("kamerTypes",kamerTypes);
        model.addAttribute("kamer",kamer);
        return "layouts/beheren/kamerAanpassen";
    }

    @PostMapping("/wijzigKamer")
    public String WijzigKamer( Model model,@ModelAttribute("kamer") Kamer kamer){
        MeldingDto melding= new MeldingDto();
        kamerRepository.update(kamer.getKamerID(),
        kamer.getKamerTypeID(),kamer.getKamerNummer());
        melding.setMelding("kamer is gewijzigd");
        ArrayList<KamerOnbeschikbaar> kamers=kamerRepository.getWithKamerOnbeschikbaar();
        model.addAttribute("kamers",kamers);
        model.addAttribute("melding",melding);
        return "layouts/beheren/kamers";
    }

    @RequestMapping("/kamerVerwijderen")
    public String KamerVerwijderen(Model model, HttpServletRequest request){
        MeldingDto melding=new MeldingDto();
        int kamerId= Integer.parseInt((request.getParameter("kamerId")));
        Boeking boeking= boekingRepository.getByKamerId(kamerId);
        if (boeking.getDatumVan() !=null && boeking.getDatumTot()!=null)
        {
            melding.setFoutmelding("Attentie! Deze kamer is reeds geboekt:");
            melding.setBoekingDatumVan(boeking.getDatumVan());
            melding.setBoekingDatumTot(boeking.getDatumTot());
            melding.setKamer(true);
            model.addAttribute("melding",melding);
            return "layouts/beheren/boodschap";
        }
        kamerOnbeschikbaarRepository.delete(kamerId);
        prijsRepository.prijsVerwijderen(kamerId);
        kamerRepository.delete(kamerId);
        ArrayList<KamerOnbeschikbaar> kamers=kamerRepository.getWithKamerOnbeschikbaar();
        model.addAttribute("kamers",kamers);
        model.addAttribute("melding",melding);
        return "layouts/beheren/kamers";
    }

    @RequestMapping("/kamerBeschikbaarheid")
    public String KamerBeschikabaarheid(Model model, HttpServletRequest request) throws ParseException {
        int kamerId= Integer.parseInt(request.getParameter("kamerId"));
        KamerOnbeschikbaar kamer=kamerOnbeschikbaarRepository.getByKamerId(kamerId);
        model.addAttribute("kamer",kamer);
        return "layouts/beheren/kamerBeschikbaarheid";
    }


    @RequestMapping("/kamerBeschikbaarMaken")
    public String KamerBeschikabaarMaken(Model model, HttpServletRequest request){
        MeldingDto melding=new MeldingDto();
        int kamerId= Integer.parseInt(request.getParameter("kamerId"));
        kamerOnbeschikbaarRepository.delete(kamerId);
        ArrayList<KamerOnbeschikbaar> kamers=kamerRepository.getWithKamerOnbeschikbaar();
        model.addAttribute("kamers",kamers);
        model.addAttribute("melding",melding);
        return "layouts/beheren/kamers";
    }

    @PostMapping("/kamerOnBeschikbaarMaken")
    public String OnbeschikbaarMaken( Model model,@ModelAttribute("KamerDto") KamerDto kamer){
        KamerOnbeschikbaar kamerOnbeschikbaar= kamerOnbeschikbaarRepository.getByKamerId(kamer.getKamerID());
       if (kamerOnbeschikbaar.getDatumVan()!=null)
       {
           kamerOnbeschikbaarRepository.update(kamer.getKamerID(),
                                           kamer.getDatumVan(),kamer.getDatumTot());
       }else {
           kamerOnbeschikbaarRepository.create(kamer.getKamerID(),
                   kamer.getDatumVan(), kamer.getDatumTot());
       }
           ArrayList<KamerOnbeschikbaar> kamers=kamerRepository.getWithKamerOnbeschikbaar();
           model.addAttribute("kamers",kamers);
           return "layouts/beheren/kamers";

    }
    @RequestMapping("/nieweKamerToeveogen")
    public String NieuweKamerToevoegen(Model model){
        MeldingDto melding=new MeldingDto();
        ArrayList<KamerType> kamerTypes=kamerTypeRepository.getLijstKamerTypes();
        KamerDto kamer=new KamerDto();
        kamer.setKamerTypes(kamerTypes);
        model.addAttribute("kamer",kamer);
        model.addAttribute("melding", melding);
        return "layouts/beheren/kamerToevoegen";
    }

    @PostMapping("/KamerToevoegen")
    public String KamerToevoegen( Model model,@ModelAttribute("KamerDto") KamerDto kamer){
        MeldingDto melding=new MeldingDto();

        if (kamerRepository.checkIfKamernummerExists(kamer.getKamerNummer()) == false) {
            kamerRepository.create(kamer.getKamerNummer(), kamer.getKamerTypeID());
            ArrayList<KamerType> kamerTypes=kamerTypeRepository.getLijstKamerTypes();
            kamer.setKamerNummer(0);
            melding.setMelding("Nieuwe kamer is toegevoegd");
            ArrayList<KamerOnbeschikbaar> kamers=kamerRepository.getWithKamerOnbeschikbaar();
            model.addAttribute("kamers",kamers);
            model.addAttribute("melding", melding);
            return "layouts/beheren/kamers";

        }else {
            ArrayList<KamerType> kamerTypes = kamerTypeRepository.getLijstKamerTypes();
            kamer.setKamerTypes(kamerTypes);
            melding.setTitel(" ");
            melding.setFoutmelding("Attentie! Deze kamernummer is reeds in gebruik");
            model.addAttribute("melding", melding);
            model.addAttribute("kamer", kamer);
            return "layouts/beheren/kamerToevoegen";
        }
    }
    @PostMapping("/KamerTypeToevoegen")
    public String KamerTypeToevoegen( Model model,@ModelAttribute("KamerBeheer") KamerDto kamer){
        kamerTypeRepository.KamerTypeToevoegen (kamer.getOmschrijving());
        ArrayList<KamerType> kamerTypes=kamerTypeRepository.getLijstKamerTypes();
        kamer.setKamerTypes(kamerTypes);
        model.addAttribute("kamer",kamer);
        return "layouts/beheren/kamerToevoegen";
    }

    //region Arrangementen
    @RequestMapping("/arrangementen")
    public String Arrangementen(Model model, RedirectAttributes redirectAttributes) {
        ArrayList<VerblijfsKeuze> verblijfskeuzes = verblijfsKeuzeRepository.getAlleVerblijfsKeuzes();

        model.addAttribute("verblijfskeuzes", verblijfskeuzes);

        if(redirectAttributes.containsAttribute("message")) {
            model.addAttribute("message", redirectAttributes.getAttribute("message"));
        }

        return "layouts/beheren/arrangementen";
    }

    @RequestMapping("/toevoegen/arrangement")
    public String VoegVerblijfskeuzeToe(Model model) {
        ArrangementDTO arrangementDTO = new ArrangementDTO();

        arrangementDTO.setKamerPrijzen(vulPrijzenOp(kamerRepository.getWithKamerOnbeschikbaar()));
        arrangementDTO.setDatums(setDefaultDatums(arrangementDTO.getKamerPrijzen().size()));
        model.addAttribute("arrangement", arrangementDTO);

        return "layouts/beheren/arrangementToevoegen";
    }

    @RequestMapping("/add/arrangement")
    public String addArrangement(@ModelAttribute("arrangement") ArrangementDTO arrangementDTO, RedirectAttributes redirectAttributes) {
        String message= null;

        arrangementDTO.setKamerPrijzen(getKamerPrijzenMetDatums(arrangementDTO.getKamerPrijzen(),arrangementDTO.getDatums()));

        int verblijfskeuzeID = verblijfsKeuzeRepository.addVerblijfskeuze(arrangementDTO.getVerblijfsKeuze());
        int prijsKamersToegevoegd = prijsRepository.voegPrijsToeVoorVerblijfskeuze(arrangementDTO.getKamerPrijzen(), verblijfskeuzeID);

        if(prijsKamersToegevoegd==0 && verblijfskeuzeID > 0) {
            message = "Het arrangement is succesvol aangemaakt maar er ging iets mis bij het toevoegen van de prijzen voor het arrangement. Gelieve deze na te kijken.";
        }
        else if(verblijfskeuzeID >0 && prijsKamersToegevoegd>0) {
            message = "Arrangement is succesvol aangemaakt.";
        }
        else {
            message = "Er ging iets mis bij het aanmaken van het arrangement.";
        }

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/arrangementen";
    }

    @RequestMapping("/arrangement")
    public String Arrangement(Model model, HttpServletRequest request) {
        VerblijfsKeuze verblijfskeuze = verblijfsKeuzeRepository.getVerblijfkeuze(Integer.parseInt(request.getParameter("verblijfskeuzeID")));

        ArrangementDTO arrangementDTO = new ArrangementDTO();

        arrangementDTO.setVerblijfsKeuze(verblijfskeuze);
        arrangementDTO.setKamerPrijzen(prijsRepository.getPrijzenVoorVerblijfskeuze(verblijfskeuze.getVerblijfskeuzeID()));

        arrangementDTO.setDatums(vulDatumsOp(arrangementDTO.getKamerPrijzen()));

        model.addAttribute("arrangement", arrangementDTO );

        return "layouts/beheren/arrangement";
    }

    @RequestMapping("/update/arrangement")
    public String UpdateArrangement(@ModelAttribute("arrangement") ArrangementDTO arrangementDTO, RedirectAttributes redirectAttributes) {
        String message=null;

        int rowsUpdatedVerblijfskeuze = verblijfsKeuzeRepository.updateVerblijfskeuze(arrangementDTO.getVerblijfsKeuze());
        int rowsUpdatedPrijzen = prijsRepository.updatePrijzenVoorVerblijfskeuze(getKamerPrijzenMetDatums(arrangementDTO.getKamerPrijzen(), arrangementDTO.getDatums()));

        if(rowsUpdatedVerblijfskeuze>0 && rowsUpdatedPrijzen>0) {
            message= "Verblijfskeuze is succesvol aangepast.";
        }
        else if(rowsUpdatedPrijzen==0) {
            message="Er ging iets mis bij het aanpassen van de prijzen.";
        }
        else {
            message = "Er is iets misgegaan tijdens het updaten.";
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/arrangementen";
    }

    @RequestMapping("/delete/arrangement")
    public String VerwijderArrangement(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String message=null;
        int verblijfskeuzeID = Integer.parseInt(request.getParameter("verblijfskeuzeID"));

        VerblijfsKeuze verblijfsKeuze = verblijfsKeuzeRepository.getVerblijfkeuze(verblijfskeuzeID);
        int aantalBoekingenVoorVerblijfskeuze = boekingRepository.getAantalByVerblijfskeuzeId(verblijfskeuzeID);

        if(aantalBoekingenVoorVerblijfskeuze>0) {
            message = "Er zijn nog reservaties voor dit arrangement.";
        }
        else if(verblijfsKeuze != null) {
            prijsRepository.deletePrijsVoorVerblijfskeuze(verblijfskeuzeID);
            verblijfsKeuzeRepository.deleteVerblijfskeuze(verblijfskeuzeID);
            message ="Reservatie verwijderd.";
        }
        else {
            message ="Reservatie niet gevonden.";
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/arrangementen";
    }

    //endregion

    //region Reservering
    @RequestMapping("/reservering")
    public String Reservering(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Boeking boeking = boekingRepository.getById(Integer.parseInt(request.getParameter("Id")));

        UpdateReserveringDTO updateReserveringDTO = new UpdateReserveringDTO();
        updateReserveringDTO.setDatumVan(boeking.getDatumVan().toString());
        updateReserveringDTO.setDatumTot(boeking.getDatumTot().toString());
        updateReserveringDTO.setVerblijfskeuzeID(boeking.getVerblijfsKeuzeID());
        updateReserveringDTO.setAantalPersonen(boeking.getAantalPersonen());
        updateReserveringDTO.setKlant(boeking.getKlant());
        updateReserveringDTO.setBoekingID(Integer.parseInt(request.getParameter("Id")));
        updateReserveringDTO.setVerblijfsKeuzes(verblijfsKeuzeRepository.getAlleVerblijfsKeuzes());
        updateReserveringDTO.setPrijsKamers(kamerRepository.getByBeschikbaarheid(boeking.getVerblijfsKeuzeID(), boeking.getDatumVan(), boeking.getDatumTot()));
        updateReserveringDTO.setPrijsKamersBoeking(prijsRepository.getKamerPrijsVoorBoeking(boeking.getBoekingID(), boeking.getVerblijfsKeuzeID()));

        if(redirectAttributes.containsAttribute("message")) {
            model.addAttribute(redirectAttributes.getAttribute("message"));
        }
        model.addAttribute("reservering", updateReserveringDTO);
        return "layouts/beheren/reservering";
    }

    @RequestMapping("/update/datum/reservering")
    public String updateDatumReservering(@ModelAttribute("reservering") UpdateReserveringDTO reservering, RedirectAttributes redirectAttributes){
        Date nieuweDatumVan = Date.valueOf(reservering.getDatumVan());
        Date nieuweDatumTot = Date.valueOf(reservering.getDatumTot());
        String message = null;

        if(nieuweDatumVan.after(nieuweDatumTot) ) {
            message="De aankomst datum mag niet na de vertrek datum liggen.";
            redirectAttributes.addFlashAttribute("message", message);

            return "redirect:/reservering?Id="+reservering.getBoekingID();
        }

        List<BoekingDetail> detailsBoekingen = boekingDetailRepository.getAllesZonderBoeking(reservering.getBoekingID(), nieuweDatumVan, nieuweDatumTot);
        List<BoekingDetail> selectedBoekingDetails = boekingDetailRepository.getAllesVoorBoeking(reservering.getBoekingID());

        for (BoekingDetail detail: selectedBoekingDetails)
        {
            for(BoekingDetail toekomstDetail: detailsBoekingen) {

                if(detail.getKamerID() == toekomstDetail.getKamer().getKamerID()) {
                message="De huidige kamer is niet beschikbaar in de gekozen periode. Gelieve een ander periode of kamer te kiezen.";
                redirectAttributes.addFlashAttribute("message", message);

                return "redirect:/reservering?Id="+reservering.getBoekingID();
                }
            }
        }

        if(boekingRepository.updateDatums(nieuweDatumVan, nieuweDatumTot, reservering.getBoekingID())>0) {
            message = "Datums zijn succesvol aangepast.";
        }
        else {
            message = "Er is iets misgegaan tijdens het updaten.";
        }
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/reservering?Id="+reservering.getBoekingID();
    }

    @RequestMapping("/update/kamerToevoegen/reservering")
    public String voegKamerToeReservering(@ModelAttribute("reservering") UpdateReserveringDTO reservering, RedirectAttributes redirectAttributes){
        List<Integer> kamers = reservering.getKamers();
        int rowsAdded = 0;

        for (int kamerID: kamers) {
            boekingDetailRepository.create(reservering.getBoekingID(), kamerID);
            rowsAdded++;
        }

        String message=null;

        if(rowsAdded>0) {
            message= "Kamer is succesvol toegevoegd aan de boeking.";
        }
        else {
            message="Er ging iets mis tijdens het toevoegen van de kamer.";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/reservering?Id="+reservering.getBoekingID();
    }

    @RequestMapping("/update/reservering")
    public String updateReservering(@ModelAttribute("reservering") UpdateReserveringDTO reservering, RedirectAttributes redirectAttributes) {
        int rowsUpdated = boekingRepository.update(reservering.getAantalPersonen(),reservering.getVerblijfskeuzeID(),reservering.getBoekingID());

        reservering.setKlant(klantRepository.getKlantVoorBoeking(reservering.getBoekingID()));
        reservering.setVerblijfsKeuzes(verblijfsKeuzeRepository.getAlleVerblijfsKeuzes());

        //add a redirectAttribute so we can give a message if the update succeeded.
        //This redirectAttribute is catched in BeherenController/reservering
        String message=null;
        if(rowsUpdated>0){
            message = "Update geslaagd.";
        }
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/reservering?Id="+reservering.getBoekingID();
    }

    @RequestMapping("/delete/kamer/reservering")
    public String deleteKamerVanReservering(@ModelAttribute("reservering") UpdateReserveringDTO reservering, RedirectAttributes redirectAttributes){
        int rows = 0;

        for(int kamerID: reservering.getGeboekteKamers()) {
            boekingDetailRepository.delete(reservering.getBoekingID(), kamerID);
            rows++;
        }

        String message=null;

        if (rows>0) {
            message="Kamer is succesvol verwijderd.";
        }
        else {
            message="Er ging iets mis tijdens het verwijderen.";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/reservering?Id="+reservering.getBoekingID();
    }

    @RequestMapping("/delete/reservering")
    public String deleteReservering(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

        boekingRepository.delete(Integer.parseInt(request.getParameter("boekingID")));

        //add a redirectAttribute so we can give a message if the delete succeeded.
        //This redirectAttribute is catched in BoekingController/reserveringen
        redirectAttributes.addFlashAttribute("message", "Reservatie verwijderd.");

        return "redirect:/reserveringen";
    }
    //endregion
    //region Private Methods
    //datums moeten naar String omgezet worden om te tonen in frontend
    private List<String> vulDatumsOp(List<Prijs> kamerPrijzen) {
        ArrayList<String> datums = new ArrayList<>();

        for (Prijs prijs: kamerPrijzen) {
            String datum = prijs.getDatumVanaf().toString();
            datums.add(datum);
        }

        return datums;
    }

    //set default dates to show in frontend
    private List<String> setDefaultDatums(int aantal) {
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> datums = new ArrayList<>();
        for (int i=0;i<aantal;i++){
            String datum = simpleFormatter.format(new java.util.Date());
            datums.add(datum);
        }
        return datums;
    }

    private List<Prijs> vulPrijzenOp(ArrayList<KamerOnbeschikbaar> kamers) {
        ArrayList<Prijs> prijzenKamers = new ArrayList<>();

        for (KamerOnbeschikbaar kamer:kamers) {
            Prijs prijs = new Prijs();
            prijs.setKamer(kamer.getKamer());
            prijs.setKamerID(kamer.getKamerID());
            prijs.setPrijsPerKamer(new BigDecimal(0));

            prijzenKamers.add(prijs);
        }
        return prijzenKamers;
    }

    private List<Prijs> getKamerPrijzenMetDatums(List<Prijs> kamerPrijzen, List<String> datums) {

        for(int i=0;i<kamerPrijzen.size();i++) {
            java.util.Date datum = Date.valueOf(datums.get(i));
            kamerPrijzen.get(i).setDatumVanaf(datum);
        }
        return kamerPrijzen;
    }
    //endregion
}
