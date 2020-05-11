package pr4.t1.kempenrust.controller;

import javafx.scene.input.DataFormat;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pr4.t1.kempenrust.model.*;
import pr4.t1.kempenrust.model.DTO.ArrangementDTO;
import pr4.t1.kempenrust.model.DTO.UpdateReserveringDTO;
import pr4.t1.kempenrust.repository.BoekingDetailRepository;
import pr4.t1.kempenrust.repository.BoekingRepository;
import pr4.t1.kempenrust.repository.KlantRepository;
import pr4.t1.kempenrust.repository.VerblijfsKeuzeRepository;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pr4.t1.kempenrust.DTO.KamerBeheer;
import pr4.t1.kempenrust.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;

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
//    Hier komen alle methodes in die iets te maken hebben met het beheren (CRUD) van het hotel
    @RequestMapping("/kamers")
    public String Kamers(Model model) {
        ArrayList<KamerBeheer> kamers=kamerRepository.getAlleKamers();
        model.addAttribute("kamers",kamers);
        return "layouts/beheren/kamers";
    }
    @RequestMapping("/kamerAanpassen")
    public String kamerAanpassen(Model model, HttpServletRequest request){
    int kamerId= Integer.parseInt((request.getParameter("kamerId")));
    KamerBeheer kamer=kamerTypeRepository.getKamerByID(kamerId);
    ArrayList<KamerType> kamerTypes=kamerTypeRepository.getLijstKamerTypes();
    kamer.setKamerTypes(kamerTypes);
    model.addAttribute("kamer",kamer);
    return "layouts/beheren/kamerAanpassen";
}
    @PostMapping("/wijzigKamer")
    public String WijzigKamer( Model model,@ModelAttribute("kamer") Kamer kamer){
        kamerRepository.WijzigKamer(kamer.getKamerID(),kamer.getKamerTypeID(),kamer.getKamerNummer());
        ArrayList<KamerBeheer> kamers=kamerRepository.getAlleKamers();
        model.addAttribute("kamers",kamers);
        return "layouts/beheren/kamers";
}
    @RequestMapping("/kamerVerwijderen")
    public String KamerVerwijderen(Model model, HttpServletRequest request){
        KamerBeheer kamer=null;
        int kamerId= Integer.parseInt((request.getParameter("kamerId")));
        kamer= boekingRepository.getGeboekteKamer(kamerId);
        if (kamer.getDatumVan() !=null && kamer.getDatumTot()!=null)
        {
            String omschrijving="Kamer reeds geboekt ";
            kamer.setOmschrijving(omschrijving);
            model.addAttribute("kamer",kamer);
            return "layouts/beheren/boodschap";
        }
        kamerOnbeschikbaarRepository.maakKamerBeschikbaarByID (kamerId);
        prijsRepository.kamerTeVerwijderen(kamerId);
        kamerRepository.KamerVerwijderen(kamerId);
        ArrayList<KamerBeheer> kamers=kamerRepository.getAlleKamers();
        model.addAttribute("kamers",kamers);
        return "layouts/beheren/kamers";
    }
    @RequestMapping("/kamerBeschikbaarheid")
    public String KamerBeschikabaarheid(Model model, HttpServletRequest request) throws ParseException {
    int kamerId= Integer.parseInt((request.getParameter("kamerId")));
    KamerBeheer kamer=kamerOnbeschikbaarRepository.getOnbeschikbaarKamerByID(kamerId);
    model.addAttribute("kamer",kamer);
    return "layouts/beheren/kamerBeschikbaarheid";
}
    @RequestMapping("/kamerBeschikbaarMaken")
    public String KamerBeschikabaarMaken(Model model, HttpServletRequest request){
        int kamerId= Integer.parseInt((request.getParameter("kamerId")));
        kamerOnbeschikbaarRepository.KamerBeschikbaarMaken(kamerId);
        ArrayList<KamerBeheer> kamers=kamerRepository.getAlleKamers();
        model.addAttribute("kamers",kamers);
        return "layouts/beheren/kamers";
    }
    @PostMapping("/kamerOnBeschikbaarMaken")
    public String OnbeschikbaarMaken( Model model,@ModelAttribute("KamerBeheer") KamerBeheer kamer){
        kamerOnbeschikbaarRepository.KamerOnbechikbaarMaken(kamer.getKamerID() ,kamer.getDatumVan(),kamer.getDatumTot());
        ArrayList<KamerBeheer> kamers=kamerRepository.getAlleKamers();
        model.addAttribute("kamers",kamers);
        return "layouts/beheren/kamers";
    }
    @RequestMapping("/nieweKamerToeveogen")
    public String NieuweKamerToevoegen(Model model){
        ArrayList<KamerType> kamerTypes=kamerTypeRepository.getLijstKamerTypes();
        KamerBeheer kamer=new KamerBeheer();
        kamer.setKamerTypes(kamerTypes);
        model.addAttribute("kamer",kamer);
        return "layouts/beheren/kamerToevoegen";
    }

    @PostMapping("/KamerToevoegen")
    public String KamerTovoegen( Model model,@ModelAttribute("KamerBeheer") KamerBeheer kamer){
        Kamer gevondenKamer=kamerRepository.getKamerByKamernummer(kamer.getKamerNummer());
        if (gevondenKamer.getKamerID() == 0) {
            kamerRepository.KamerToevoegen(kamer.getKamerNummer(), kamer.getKamerTypeID());
            ArrayList<KamerType> kamerTypes=kamerTypeRepository.getLijstKamerTypes();
            kamer.setKamerNummer(0);
            kamer.setMelding("Nieuwe kamer is toegevoegd");
            kamer.setKamerTypes(kamerTypes);
            model.addAttribute("kamer",kamer);
            return "layouts/beheren/kamerToevoegen";
        }else {
            ArrayList<KamerType> kamerTypes = kamerTypeRepository.getLijstKamerTypes();
            kamer.setKamerTypes(kamerTypes);
            kamer.setTitel("Attentie");
            kamer.setFoutMelding("Deze kamernummer is reeds in gebruik");
            model.addAttribute("kamer", kamer);
            return "layouts/beheren/kamerToevoegen";
        }
    }
    @PostMapping("/KamerTypeToevoegen")
    public String KamerTypeTovoegen( Model model,@ModelAttribute("KamerBeheer") KamerBeheer kamer){
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

        List<Kamer> kamers = kamerRepository.getAlleKamersMetModel();

        ArrayList<Prijs> prijzenKamers = new ArrayList<>();
        for (Kamer kamer:kamers) {
            Prijs prijs = new Prijs();
            prijs.setKamer(kamer);
            prijs.setKamerID(kamer.getKamerID());
            prijs.setPrijsPerKamer(new BigDecimal(0));

            prijzenKamers.add(prijs);
        }
        arrangementDTO.setKamerPrijzen(prijzenKamers);

        model.addAttribute("arrangement", arrangementDTO);

        return "layouts/beheren/arrangementToevoegen";
    }

    @RequestMapping("/add/arrangement")
    public String addArrangement(@ModelAttribute("arrangement") ArrangementDTO arrangementDTO, RedirectAttributes redirectAttributes) {
        String message= null;

        for(int i=0;i<arrangementDTO.getKamerPrijzen().size();i++) {
            java.util.Date datum = Date.valueOf(arrangementDTO.getDatums().get(i));
            arrangementDTO.getKamerPrijzen().get(i).setDatumVanaf(datum);
        }


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
        model.addAttribute("arrangement", arrangementDTO );

        return "layouts/beheren/arrangement";
    }

    @RequestMapping("/update/arrangement")
    public String UpdateArrangement(@ModelAttribute("arrangement") ArrangementDTO arrangementDTO, RedirectAttributes redirectAttributes) {
        String message=null;
        int rowsUpdated = verblijfsKeuzeRepository.updateVerblijfskeuze(arrangementDTO.getVerblijfsKeuze());

        if(rowsUpdated>0) {
            message= "Verblijfskeuze is succesvol aangepast.";
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
        int aantalBoekingenVoorVerblijfskeuze = boekingRepository.getAantalBoekingenVoorVerblijfskeuze(verblijfskeuzeID);

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

    @RequestMapping("/reservering")
    public String Reservering(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Boeking boeking = boekingRepository.getBoeking(Integer.parseInt(request.getParameter("Id")));

        UpdateReserveringDTO updateReserveringDTO = new UpdateReserveringDTO();
        updateReserveringDTO.setDatumVan(boeking.getDatumVan().toString());
        updateReserveringDTO.setDatumTot(boeking.getDatumTot().toString());
        updateReserveringDTO.setVerblijfskeuzeID(boeking.getVerblijfsKeuzeID());
        updateReserveringDTO.setAantalPersonen(boeking.getAantalPersonen());
        updateReserveringDTO.setKlant(boeking.getKlant());
        updateReserveringDTO.setBoekingID(Integer.parseInt(request.getParameter("Id")));
        updateReserveringDTO.setVerblijfsKeuzes(verblijfsKeuzeRepository.getAlleVerblijfsKeuzes());
        updateReserveringDTO.setPrijsKamers(kamerRepository.getAlleVrijeKamers(boeking.getVerblijfsKeuzeID(), boeking.getDatumVan(), boeking.getDatumTot()));
        updateReserveringDTO.setPrijsKamersBoeking(kamerRepository.getKamerPrijsVoorBoeking(boeking.getBoekingID(), boeking.getVerblijfsKeuzeID()));

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

        List<BoekingDetail> detailsBoekingen = boekingDetailRepository.getBoekingenZonderHuidigeBoeking(reservering.getBoekingID(), nieuweDatumVan, nieuweDatumTot);
        List<BoekingDetail> selectedBoekingDetails = boekingDetailRepository.getDetailsVoorBoeking(reservering.getBoekingID());

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

        if(boekingRepository.updateBoekingDatums(nieuweDatumVan, nieuweDatumTot, reservering.getBoekingID())>0) {
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
        int rowsAdded = boekingRepository.voegKamerToeAanBoeking(reservering.getBoekingID(),kamers);

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
        int rowsUpdated = boekingRepository.updateBoeking(reservering.getAantalPersonen(),reservering.getVerblijfskeuzeID(),reservering.getBoekingID());

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
        int rows = boekingRepository.verwijderKamerVanBoeking(reservering.getBoekingID(),reservering.getGeboekteKamers());
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

        boekingRepository.deleteBoeking(Integer.parseInt(request.getParameter("boekingID")));

        //add a redirectAttribute so we can give a message if the delete succeeded.
        //This redirectAttribute is catched in BoekingController/reserveringen
        redirectAttributes.addFlashAttribute("message", "Reservatie verwijderd.");

        return "redirect:/reserveringen";
    }


}
