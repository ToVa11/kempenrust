package pr4.t1.kempenrust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pr4.t1.kempenrust.model.*;
import pr4.t1.kempenrust.model.DTO.ReserveringDto;
import pr4.t1.kempenrust.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
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
    public String ReserveringBevestigen(@ModelAttribute("reserveringDetails") ReserveringDto reserveringDetails,  Model model) {
        if(klantRepository.customerExists(reserveringDetails.getEmail()) == false) {
            klantRepository.createCustomer(
                    reserveringDetails.getVoornaam(),
                    reserveringDetails.getNaam(),
                    reserveringDetails.getTelefoon(),
                    reserveringDetails.getEmail());
        }
        Klant klant = klantRepository.getCustomerByEmail(reserveringDetails.getEmail());

        var datumAankomst = Date.valueOf(reserveringDetails.getDatumAankomst());
        var datumVertrek = (reserveringDetails.getDatumVertrek() != "")
                ? Date.valueOf(reserveringDetails.getDatumVertrek())
                : null;
        BigDecimal bedragVoorschot = new BigDecimal(0);

        int BoekingID = boekingRepository.createReservation(
                klant.getKlantID(),
                reserveringDetails.getKeuzeArrangement(),
                bedragVoorschot,
                reserveringDetails.getAantalPersonen(),
                datumAankomst,
                datumVertrek);

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
    public String AfgelopenReserveringen() {
        return "layouts/boeking/afgelopen_reservaties";
    }
}
