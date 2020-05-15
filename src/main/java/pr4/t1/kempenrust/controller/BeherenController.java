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

    @RequestMapping("/nieuweKlantToevoegen")
    public String NieuweKlantToevogen(Model model){
        MeldingDto melding= new MeldingDto();
        Klant klant=new Klant();
        model.addAttribute("klant",klant);
        model.addAttribute("melding",melding);
        return "layouts/beheren/klantToevoegen";
    }

    @PostMapping("/nieuweKlantToevoegen")
    public String NieuweKlantToevoegen( Model model,@ModelAttribute("klant") Klant klant){
      MeldingDto melding= new MeldingDto();
      var result=  klantRepository.klantToevoegen( klant.getVoornaam(),
                klant.getNaam(), klant.getTelefoonnummer(),
                klant.getEmail(),klant.getStraat(), klant.getHuisnummer(),
                klant.getPostcode(),klant.getGemeente());

        if (result > 0)
        {
            melding.setMelding("Nieuwe klant is toegevoegd");
            model.addAttribute("melding",melding);
            ArrayList<Klant> klanten=klantRepository.getKlanten();
            model.addAttribute("klanten",klanten);
            return "layouts/beheren/klanten";
        }
        melding.setFoutmelding("Attentie! Nieuwe klant is niet toegevoegd");
        model.addAttribute("melding",melding);
        model.addAttribute("klant",klant);
        return "layouts/beheren/klantToevoegen";
    }


    @RequestMapping("/KlantgegevensAanpassen")
    public String klantAanpassen(Model model, HttpServletRequest request){
        int klantId= Integer.parseInt((request.getParameter("klantId")));
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
        int klantId= Integer.parseInt((request.getParameter("klantId")));
        Boeking boeking = boekingRepository.getBoekingVoorKlant(klantId);
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
        ArrayList<KamerDto> kamers=kamerRepository.getKamers();
        model.addAttribute("kamers",kamers);
        model.addAttribute("melding",melding);
        return "layouts/beheren/kamers";
    }

    @RequestMapping("/kamerAanpassen")
    public String kamerAanpassen(Model model, HttpServletRequest request){
        int kamerId= Integer.parseInt((request.getParameter("kamerId")));
        KamerDto kamer=kamerTypeRepository.getKamerByID(kamerId);
        ArrayList<KamerType> kamerTypes=kamerTypeRepository.getLijstKamerTypes();
        kamer.setKamerTypes(kamerTypes);
        model.addAttribute("kamer",kamer);
        return "layouts/beheren/kamerAanpassen";
    }

    @PostMapping("/wijzigKamer")
    public String WijzigKamer( Model model,@ModelAttribute("kamer") Kamer kamer){
        MeldingDto melding= new MeldingDto();

        kamerRepository.WijzigKamer(kamer.getKamerID(),
        kamer.getKamerTypeID(),kamer.getKamerNummer());
        melding.setMelding("kamer is gewijzigd");
        ArrayList<KamerDto> kamers=kamerRepository.getKamers();
        model.addAttribute("kamers",kamers);
        model.addAttribute("melding",melding);
        return "layouts/beheren/kamers";
    }

    @RequestMapping("/kamerVerwijderen")
    public String KamerVerwijderen(Model model, HttpServletRequest request){
        MeldingDto melding=new MeldingDto();
        int kamerId= Integer.parseInt((request.getParameter("kamerId")));
        Boeking boeking= boekingRepository.getBoekingVoorKamer(kamerId);
        if (boeking.getDatumVan() !=null && boeking.getDatumTot()!=null)
        {
            melding.setFoutmelding("Attentie! Deze kamer is reeds geboekt:");
            melding.setBoekingDatumVan(boeking.getDatumVan());
            melding.setBoekingDatumTot(boeking.getDatumTot());
            melding.setKamer(true);
            model.addAttribute("melding",melding);
            return "layouts/beheren/boodschap";
        }
        kamerOnbeschikbaarRepository.KamerBeschikbaarMaken(kamerId);
//        prijsRepository.kamerTeVerwijderen(kamerId);
        prijsRepository.prijsVerwijderen(kamerId);
        kamerRepository.KamerVerwijderen(kamerId);
        ArrayList<KamerDto> kamers=kamerRepository.getKamers();
        model.addAttribute("kamers",kamers);
        model.addAttribute("melding",melding);
        return "layouts/beheren/kamers";
    }

    @RequestMapping("/kamerBeschikbaarheid")
    public String KamerBeschikabaarheid(Model model, HttpServletRequest request) throws ParseException {
        int kamerId= Integer.parseInt(request.getParameter("kamerId"));
        KamerOnbeschikbaar kamer=kamerOnbeschikbaarRepository.getOnbeschikbaarKamerByID(kamerId);
        model.addAttribute("kamer",kamer);
        return "layouts/beheren/kamerBeschikbaarheid";
    }


    @RequestMapping("/kamerBeschikbaarMaken")
    public String KamerBeschikabaarMaken(Model model, HttpServletRequest request){
        MeldingDto melding=new MeldingDto();
        int kamerId= Integer.parseInt(request.getParameter("kamerId"));
        kamerOnbeschikbaarRepository.KamerBeschikbaarMaken(kamerId);
        ArrayList<KamerDto> kamers=kamerRepository.getKamers();
        model.addAttribute("kamers",kamers);
        model.addAttribute("melding",melding);
        return "layouts/beheren/kamers";
    }

    @PostMapping("/kamerOnBeschikbaarMaken")
    public String OnbeschikbaarMaken( Model model,@ModelAttribute("KamerDto") KamerDto kamer){
       // MeldingDto melding=new MeldingDto();
        KamerOnbeschikbaar kamerOnbeschikbaar= kamerOnbeschikbaarRepository.getOnbeschikbaarKamerByID(kamer.getKamerID());
       if (kamerOnbeschikbaar.getDatumVan()!=null)
       {
           kamerOnbeschikbaarRepository.wijzigOnbeschikbaarheid(kamer.getKamerID(),
                                           kamer.getDatumVan(),kamer.getDatumTot());
       }else {
           kamerOnbeschikbaarRepository.KamerOnbeschikbaarMaken(kamer.getKamerID(),
                   kamer.getDatumVan(), kamer.getDatumTot());
       }
           ArrayList<KamerDto> kamers=kamerRepository.getKamers();
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
        Kamer gevondenKamer=kamerRepository.getKamerByKamernummer(kamer.getKamerNummer());
        if (gevondenKamer.getKamerID() == 0) {
            kamerRepository.KamerToevoegen(kamer.getKamerNummer(), kamer.getKamerTypeID());
            ArrayList<KamerType> kamerTypes=kamerTypeRepository.getLijstKamerTypes();
            kamer.setKamerNummer(0);
            melding.setMelding("Nieuwe kamer is toegevoegd");
            ArrayList<KamerDto> kamers=kamerRepository.getKamers();
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


    @RequestMapping("/arrangementen")
    public String Arrangementen() {
        return "layouts/beheren/arrangementen";
    }

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
