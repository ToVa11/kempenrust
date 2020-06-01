package pr4.t1.kempenrust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pr4.t1.kempenrust.Helpers.helper;
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

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BeherenController {
    //region DI Repos
    @Autowired
    private BoekingDetailRepository boekingDetailRepository;
    @Autowired
    private VerblijfsKeuzeRepository verblijfsKeuzeRepository;
    @Autowired
    private BoekingRepository boekingRepository;
    @Autowired
    private KlantRepository klantRepository;
    @Autowired
    private KamerRepository kamerRepository;
    @Autowired
    private KamerTypeRepository kamerTypeRepository;
    @Autowired
    private KamerOnbeschikbaarRepository kamerOnbeschikbaarRepository;
    @Autowired
    private PrijsRepository prijsRepository;

    //endregion

    //region Klanten
    @RequestMapping("/klanten")
    public String getKlanten(Model model) {
        MeldingDto melding= new MeldingDto();
        ArrayList<Klant> klanten=klantRepository.get();
        model.addAttribute("melding",melding);
        model.addAttribute("klanten",klanten);
        return "layouts/beheren/klanten";
    }

    @RequestMapping("/KlantgegevensAanpassen")
    public String getKlantForm(Model model, HttpServletRequest request){
        int klantId= Integer.parseInt(request.getParameter("klantId"));
        Klant klant=klantRepository.getById(klantId);
        model.addAttribute("klant",klant);
        return "layouts/beheren/klantgegevensAanpassen";
    }

    @PostMapping("/wijzigKlant")
    public String updateKlant(Model model, @ModelAttribute("klant") Klant klant){
        MeldingDto melding= new MeldingDto();
        klantRepository.update(klant.getKlantID(), klant.getVoornaam(),
                        klant.getNaam(), klant.getTelefoonnummer(),
                        klant.getEmail(),klant.getStraat(), klant.getHuisnummer(),
                        klant.getPostcode(),klant.getGemeente());

        ArrayList<Klant> klanten=klantRepository.get();
        model.addAttribute("klanten",klanten);
        model.addAttribute("melding",melding);
        return "layouts/beheren/klanten";
    }

    @RequestMapping("/klantVerwijderen")
    public String deleteKlant(Model model, HttpServletRequest request){
        MeldingDto melding=new MeldingDto();
        int klantId= Integer.parseInt(request.getParameter("klantId"));
        Boeking boeking = boekingRepository.getByKlantId(klantId);
        if (boeking.getDatumVan() !=null && boeking.getDatumTot()!=null)
        {
            melding.setMelding("Er zijn nog reservaties voor deze klant.");
            ArrayList<Klant> klanten=klantRepository.get();
            model.addAttribute("klanten",klanten);
            model.addAttribute("melding",melding);
            return "layouts/beheren/klanten";
        }
        klantRepository.delete(klantId);
        melding.setMelding("De klant is verwijderd.");
        ArrayList<Klant> klanten=klantRepository.get();
        model.addAttribute("klanten",klanten);
        model.addAttribute("melding",melding);
        return "layouts/beheren/klanten";
    }
    //endregion

    //region Kamers
    @RequestMapping("/kamers")
    public String getKamers(Model model) {
        MeldingDto melding=new MeldingDto();
        ArrayList<KamerOnbeschikbaar> kamers=kamerRepository.getWithKamerOnbeschikbaar();
        model.addAttribute("kamers",kamers);
        model.addAttribute("melding",melding);
        return "layouts/beheren/kamers";
    }

    @RequestMapping("/nieweKamerToeveogen")
    public String getKamerCreateForm(Model model){
        MeldingDto melding=new MeldingDto();
        ArrayList<KamerType> kamerTypes=kamerTypeRepository.get();
        KamerDto kamer=new KamerDto();
        kamer.setWijziging(false);
        kamer.setKamerTypes(kamerTypes);
        model.addAttribute("kamer",kamer);
        model.addAttribute("melding", melding);
        return "layouts/beheren/kamerToevoegen";
    }

    @PostMapping("/KamerToevoegen")
    public String createKamer(Model model, @ModelAttribute("KamerDto") KamerDto kamer){
        MeldingDto melding=new MeldingDto();

        if (kamerRepository.existsByKamerNummer(kamer.getKamerNummer()) == false) {
            kamerRepository.create(kamer.getKamerNummer(), kamer.getKamerTypeID());
            ArrayList<KamerType> kamerTypes=kamerTypeRepository.get();
            kamer.setKamerNummer(0);
            melding.setMelding("Nieuwe kamer is toegevoegd");
            ArrayList<KamerOnbeschikbaar> kamers=kamerRepository.getWithKamerOnbeschikbaar();
            model.addAttribute("kamers",kamers);
            model.addAttribute("melding", melding);
            return "layouts/beheren/kamers";

        }else {
            ArrayList<KamerType> kamerTypes = kamerTypeRepository.get();
            kamer.setKamerTypes(kamerTypes);
            melding.setTitel(" ");
            melding.setFoutmelding("Attentie! Deze kamernummer is reeds in gebruik");
            model.addAttribute("melding", melding);
            model.addAttribute("kamer", kamer);
            return "layouts/beheren/kamerToevoegen";
        }
    }

    @RequestMapping("/kamerAanpassen")
    public String getKamerUpdateForm(Model model, HttpServletRequest request){
        int kamerId= Integer.parseInt(request.getParameter("kamerId"));
        Kamer selectedkamer=kamerRepository.getById(kamerId);
        ArrayList<KamerType> kamerTypes=kamerTypeRepository.get();
        KamerDto kamer=new KamerDto();
        kamer.setKamerID(selectedkamer.getKamerID());
        kamer.setKamerNummer(selectedkamer.getKamerNummer());
        kamer.setKamerTypes(kamerTypes);
        kamer.setWijziging(true);
        model.addAttribute("kamerTypes",kamerTypes);
        model.addAttribute("kamer",kamer);
        return "layouts/beheren/kamerAanpassen";
    }

    @PostMapping("/wijzigKamer")
    public String updateKamer(Model model, @ModelAttribute("kamer") Kamer kamer){
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
    public String deleteKamer(Model model, HttpServletRequest request){
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
        prijsRepository.deleteByKamerId(kamerId);
        kamerRepository.delete(kamerId);
        ArrayList<KamerOnbeschikbaar> kamers=kamerRepository.getWithKamerOnbeschikbaar();
        model.addAttribute("kamers",kamers);
        model.addAttribute("melding",melding);
        return "layouts/beheren/kamers";
    }
    //endregion

    //region Kamertypes
    @PostMapping("/KamerTypeToevoegen")
    public String createKamerType(Model model, @ModelAttribute("KamerBeheer") KamerDto kamer){
        MeldingDto melding=new MeldingDto();
        kamerTypeRepository.create(kamer.getOmschrijving());
        ArrayList<KamerType> kamerTypes=kamerTypeRepository.get();
        kamer.setKamerTypes(kamerTypes);
        melding.setMelding("Nieuwe kamertype is toegevoegd");
        model.addAttribute("kamer",kamer);
        model.addAttribute("melding",melding);

        if (kamer.getWijziging() == false)
        {
            return "layouts/beheren/kamerToevoegen";
        }
        else
        {
            return "layouts/beheren/kamerAanpassen";
        }

    }

    //endregion

    //region KamerOnbeschikbaar
    @RequestMapping("/kamerBeschikbaarheid")
    public String getKamersOnbeschikbaar(Model model, HttpServletRequest request) throws ParseException {
        int kamerId= Integer.parseInt(request.getParameter("kamerId"));
        KamerOnbeschikbaar onbeschikbaarkamer=kamerOnbeschikbaarRepository.getByKamerId(kamerId);
        KamerDto kamer = new KamerDto();
        kamer.setKamerID(onbeschikbaarkamer.getKamerID());
        kamer.setKamerNummer(onbeschikbaarkamer.getKamer().getKamerNummer());
        kamer.setDatumVan(onbeschikbaarkamer.getDatumVan());
        kamer.setDatumTot(onbeschikbaarkamer.getDatumTot());
        model.addAttribute("kamer",kamer);
        return "layouts/beheren/kamerBeschikbaarheid";
    }

    @PostMapping("/kamerOnBeschikbaarMaken")
    public String createKamerOnbeschikbaar(Model model, @ModelAttribute("KamerDto") KamerDto kamer){
        KamerOnbeschikbaar kamerOnbeschikbaar= kamerOnbeschikbaarRepository.getByKamerId(kamer.getKamerID());
        MeldingDto melding=new MeldingDto();
        var datumVandag = Date.valueOf(LocalDate.now());
        if(kamer.getDatumVan().before(datumVandag) || kamer.getDatumVan().after(kamer.getDatumTot()))
        {
            melding.setFoutmelding("Gelieve de datums te controleren");
            kamer.setKamerNummer(kamerOnbeschikbaar.getKamer().getKamerNummer());
            model.addAttribute("melding",melding);
            model.addAttribute("kamer",kamer);
            return "layouts/beheren/kamerBeschikbaarheid";
        }
        if (kamerOnbeschikbaar.getDatumVan()!=null)
        {
            kamerOnbeschikbaarRepository.update(kamer.getKamerID(),
            kamer.getDatumVan(),kamer.getDatumTot());
        }else
        {
            kamerOnbeschikbaarRepository.create(kamer.getKamerID(),
            kamer.getDatumVan(), kamer.getDatumTot());
        }
        ArrayList<KamerOnbeschikbaar> kamers=kamerRepository.getWithKamerOnbeschikbaar();
        model.addAttribute("kamers",kamers);
        return "layouts/beheren/kamers";
    }

    @RequestMapping("/kamerBeschikbaarMaken")
    public String deleteKamerOnbeschikbaar(Model model, HttpServletRequest request){
        MeldingDto melding=new MeldingDto();
        int kamerId= Integer.parseInt(request.getParameter("kamerId"));
        kamerOnbeschikbaarRepository.delete(kamerId);
        ArrayList<KamerOnbeschikbaar> kamers=kamerRepository.getWithKamerOnbeschikbaar();
        model.addAttribute("kamers",kamers);
        model.addAttribute("melding",melding);
        return "layouts/beheren/kamers";
    }
    //endregion

    //region Arrangementen
    @RequestMapping("/arrangementen")
    public String getVerblijfskeuzes(Model model, RedirectAttributes redirectAttributes) {
        ArrayList<VerblijfsKeuze> verblijfskeuzes = verblijfsKeuzeRepository.get();

        model.addAttribute("verblijfskeuzes", verblijfskeuzes);

        if(redirectAttributes.containsAttribute("message")) {
            model.addAttribute("message", redirectAttributes.getAttribute("message"));
        }

        return "layouts/beheren/arrangementen";
    }

    @RequestMapping("/arrangement")
    public String getVerblijfskeuzeById(Model model, HttpServletRequest request) {
        VerblijfsKeuze verblijfskeuze = verblijfsKeuzeRepository.getById(Integer.parseInt(request.getParameter("verblijfskeuzeID")));

        ArrangementDTO arrangementDTO = new ArrangementDTO();

        arrangementDTO.setVerblijfsKeuze(verblijfskeuze);
        arrangementDTO.setKamerPrijzen(prijsRepository.getByVerblijfskeuzeId(verblijfskeuze.getVerblijfskeuzeID()));

        arrangementDTO.setDatums(vulDatumsOp(arrangementDTO.getKamerPrijzen()));

        model.addAttribute("arrangement", arrangementDTO );

        return "layouts/beheren/arrangement";
    }

    @RequestMapping("/toevoegen/arrangement")
    public String getVerblijfskeuzeForm(Model model) {
        ArrangementDTO arrangementDTO = new ArrangementDTO();

        arrangementDTO.setKamerPrijzen(vulPrijzenOp(kamerRepository.getWithKamerOnbeschikbaar()));
        arrangementDTO.setDatums(setDefaultDatums(arrangementDTO.getKamerPrijzen().size()));
        model.addAttribute("arrangement", arrangementDTO);

        return "layouts/beheren/arrangementToevoegen";
    }

    @RequestMapping("/add/arrangement")
    public String createVerblijfskeuze(@ModelAttribute("arrangement") ArrangementDTO arrangementDTO, RedirectAttributes redirectAttributes) {
        String message= null;

        arrangementDTO.setKamerPrijzen(getKamerPrijzenMetDatums(arrangementDTO.getKamerPrijzen(),arrangementDTO.getDatums()));

        int verblijfskeuzeID = verblijfsKeuzeRepository.create(arrangementDTO.getVerblijfsKeuze());
        int prijsKamersToegevoegd = prijsRepository.create(arrangementDTO.getKamerPrijzen(), verblijfskeuzeID);

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

    @RequestMapping("/update/arrangement")
    public String updateVerblijfsKeuze(@ModelAttribute("arrangement") ArrangementDTO arrangementDTO, RedirectAttributes redirectAttributes) {
        String message=null;

        int rowsUpdatedVerblijfskeuze = verblijfsKeuzeRepository.update(arrangementDTO.getVerblijfsKeuze());
        int rowsUpdatedPrijzen = prijsRepository.update(getKamerPrijzenMetDatums(arrangementDTO.getKamerPrijzen(), arrangementDTO.getDatums()));

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
    public String deleteVerblijfskeuze(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String message=null;
        int verblijfskeuzeID = Integer.parseInt(request.getParameter("verblijfskeuzeID"));

        VerblijfsKeuze verblijfsKeuze = verblijfsKeuzeRepository.getById(verblijfskeuzeID);
        int aantalBoekingenVoorVerblijfskeuze = boekingRepository.getAantalByVerblijfskeuzeId(verblijfskeuzeID);

        if(aantalBoekingenVoorVerblijfskeuze>0) {
            message = "Er zijn nog reservaties voor dit arrangement.";
        }
        else if(verblijfsKeuze != null) {
            prijsRepository.deleteByVerblijfskeuzeId(verblijfskeuzeID);
            verblijfsKeuzeRepository.delete(verblijfskeuzeID);
            message ="Arrangement verwijderd.";
        }
        else {
            message ="Arrangement niet gevonden.";
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/arrangementen";
    }
    //endregion

    //region Reservering
    @RequestMapping("/reservering")
    public String getBoekingen(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Boeking boeking = boekingRepository.getById(Integer.parseInt(request.getParameter("Id")));

        UpdateReserveringDTO updateReserveringDTO = new UpdateReserveringDTO();
        updateReserveringDTO.setDatumVan(boeking.getDatumVan().toString());
        updateReserveringDTO.setDatumTot(boeking.getDatumTot().toString());
        updateReserveringDTO.setVerblijfskeuzeID(boeking.getVerblijfsKeuzeID());
        updateReserveringDTO.setAantalPersonen(boeking.getAantalPersonen());
        updateReserveringDTO.setKlant(boeking.getKlant());
        updateReserveringDTO.setBoekingID(Integer.parseInt(request.getParameter("Id")));
        updateReserveringDTO.setVerblijfsKeuzes(verblijfsKeuzeRepository.get());
        updateReserveringDTO.setPrijsKamers(kamerRepository.getByBeschikbaarheid(boeking.getVerblijfsKeuzeID(), boeking.getDatumVan(), boeking.getDatumTot()));
        updateReserveringDTO.setPrijsKamersBoeking(prijsRepository.getByBoekingId(boeking.getBoekingID(), boeking.getVerblijfsKeuzeID()));

        if(redirectAttributes.containsAttribute("message")) {
            model.addAttribute(redirectAttributes.getAttribute("message"));
        }
        model.addAttribute("reservering", updateReserveringDTO);
        return "layouts/beheren/reservering";
    }

    @RequestMapping("/update/kamerToevoegen/reservering")
    public String addBoekingKamer(@ModelAttribute("reservering") UpdateReserveringDTO reservering, RedirectAttributes redirectAttributes){
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
    public String updateBoeking(@ModelAttribute("reservering") UpdateReserveringDTO reservering, RedirectAttributes redirectAttributes) {
        int rowsUpdated = boekingRepository.update(reservering.getAantalPersonen(),reservering.getVerblijfskeuzeID(),reservering.getBoekingID());

        reservering.setKlant(klantRepository.getByBoekingId(reservering.getBoekingID()));
        reservering.setVerblijfsKeuzes(verblijfsKeuzeRepository.get());

        //add a redirectAttribute so we can give a message if the update succeeded.
        //This redirectAttribute is catched in BeherenController/reservering
        String message=null;
        if(rowsUpdated>0){
            message = "Update geslaagd.";
        }
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/reservering?Id="+reservering.getBoekingID();
    }

    @RequestMapping("/update/datum/reservering")
    public String updateBoekingDatum(@ModelAttribute("reservering") UpdateReserveringDTO reservering, RedirectAttributes redirectAttributes){
        Date nieuweDatumVan = Date.valueOf(reservering.getDatumVan());
        Date nieuweDatumTot = Date.valueOf(reservering.getDatumTot());
        String message = null;
        message = helper.checkDatums(reservering.getDatumVan(), reservering.getDatumTot());

        if(message!=null) {
            redirectAttributes.addFlashAttribute("message",message);
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

    @RequestMapping("/delete/reservering")
    public String deleteBoeking(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

        boekingRepository.delete(Integer.parseInt(request.getParameter("boekingID")));

        //add a redirectAttribute so we can give a message if the delete succeeded.
        //This redirectAttribute is catched in BoekingController/reserveringen
        redirectAttributes.addFlashAttribute("message", "Reservatie verwijderd.");

        return "redirect:/reserveringen";
    }

    @RequestMapping("/delete/kamer/reservering")
    public String deleteBoekingKamer(@ModelAttribute("reservering") UpdateReserveringDTO reservering, RedirectAttributes redirectAttributes){
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
