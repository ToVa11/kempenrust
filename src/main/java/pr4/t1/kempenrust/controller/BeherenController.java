package pr4.t1.kempenrust.controller;

import javafx.scene.input.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pr4.t1.kempenrust.model.Boeking;
import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.DTO.UpdateReserveringDTO;
import pr4.t1.kempenrust.repository.BoekingDetailRepository;
import pr4.t1.kempenrust.repository.BoekingRepository;
import pr4.t1.kempenrust.repository.KlantRepository;
import pr4.t1.kempenrust.repository.VerblijfsKeuzeRepository;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pr4.t1.kempenrust.DTO.KamerBeheer;
import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.KamerOnbeschikbaar;
import pr4.t1.kempenrust.model.KamerType;
import pr4.t1.kempenrust.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

import java.util.ArrayList;

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

        if(redirectAttributes.containsAttribute("message")) {
            model.addAttribute(redirectAttributes.getAttribute("message"));
        }
        model.addAttribute("reservering", updateReserveringDTO);
        return "layouts/beheren/reservering";
    }

    @RequestMapping("/update/reservering")
    public String updateReservering(@ModelAttribute("reservering") UpdateReserveringDTO reservering, RedirectAttributes redirectAttributes) {
        int rowsUpdated = boekingRepository.updateBoeking(reservering.getDatumVan(),reservering.getDatumTot(),reservering.getAantalPersonen(),reservering.getVerblijfskeuzeID(),reservering.getBoekingID());

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

    @RequestMapping("delete/reservering")
    public String deleteReservering(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

        boekingRepository.deleteBoeking(Integer.parseInt(request.getParameter("boekingID")));

        //add a redirectAttribute so we can give a message if the delete succeeded.
        //This redirectAttribute is catched in BoekingController/reserveringen
        redirectAttributes.addFlashAttribute("message", "Reservatie verwijderd.");

        return "redirect:/reserveringen";
    }
}
