package pr4.t1.kempenrust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pr4.t1.kempenrust.DTO.BoekingDetailDto;
import pr4.t1.kempenrust.DTO.KamerBeheer;
import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.DTO.ReserveringDto;
import pr4.t1.kempenrust.model.Kamer;
import pr4.t1.kempenrust.model.Prijs;
import pr4.t1.kempenrust.model.VerblijfsKeuze;
import pr4.t1.kempenrust.repository.BoekingDetailRepository;
import pr4.t1.kempenrust.repository.KamerRepository;
import pr4.t1.kempenrust.repository.VerblijfsKeuzeRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BoekingController {
    @Autowired
    BoekingDetailRepository boekingDetailRepository;
    @Autowired
    VerblijfsKeuzeRepository verblijfsKeuzeRepository;
    @Autowired
    KamerRepository kamerRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

        var datumAankomst = Date.valueOf(reserveringDetails.getDatumAankomst());
        var datumVertrek = (reserveringDetails.getDatumVertrek() != "")
                ? Date.valueOf(reserveringDetails.getDatumVertrek())
                : null;
        reserveringDetails.setPrijsVrijeKamers(kamerRepository.getAllAvailableRooms(reserveringDetails.getKeuzeArrangement(), datumAankomst, datumVertrek));

        //Hier ga ik nog eens verblijfskeuzes ophalen, hebben jullie een betere oplossing?
        //object Dto kan geen complexe objecten doorsturen (enkel int, double, String & List (van de afgelopen 3)
        reserveringDetails.setVerblijfsKeuzes(verblijfsKeuzeRepository.getAllVerblijfsKeuzes());

        model.addAttribute("reserveringDetails", reserveringDetails);

        return "layouts/boeking/reserveren";
    }

    // via params kan ik meerdere submits in mijn form gebruiken
    @PostMapping(value = "/reserveren", params = "action=bevestigen")
    public String ReserveringBevestigen(@ModelAttribute("reserveringDetails") ReserveringDto requestReservering,  Model model, HttpServletRequest request) {
        var test = requestReservering;
        return "test";
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
    public String AfgelopenReserveringen(Model model) {
        ArrayList<BoekingDetail> details = boekingDetailRepository.getAfgelopenReservaties ();
        BoekingDetailDto boekingDetailDto=new BoekingDetailDto();
        model.addAttribute("details",details);
        model.addAttribute("boekingDetailDto",boekingDetailDto);

        return "layouts/boeking/afgelopen_reservaties";
    }

    @PostMapping("/afgelopen_reservaties")
    public String AfgelopenReserveringen(Model model,@ModelAttribute("KamerBeheer") BoekingDetailDto boekingDetailDto) {
        if (boekingDetailDto.getDatumVan() !="" && boekingDetailDto.getDatumTot() !="")
        {
            var datumVan = Date.valueOf(boekingDetailDto.getDatumVan());
            var datumTot = Date.valueOf(boekingDetailDto.getDatumTot());

            ArrayList<BoekingDetailDto> details = boekingDetailRepository
            .getAlleDetailsMetDatums(datumVan,datumTot);
            model.addAttribute("details",details);
            model.addAttribute("boekingDetailDto",boekingDetailDto);
            return "layouts/boeking/afgelopen_reservaties";
        }
        ArrayList<BoekingDetail> details = boekingDetailRepository.getAfgelopenReservaties ();
        model.addAttribute("details",details);
        model.addAttribute("boekingDetailDto",boekingDetailDto);
        return "layouts/boeking/afgelopen_reservaties";
    }
}
