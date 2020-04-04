package pr4.t1.kempenrust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.VerblijfsKeuze;
import pr4.t1.kempenrust.repository.BoekingDetailRepository;
import pr4.t1.kempenrust.repository.VerblijfsKeuzeRepository;

import java.util.ArrayList;

@Controller
public class BoekingController {
    @Autowired
    BoekingDetailRepository boekingDetailRepository;

    @Autowired
    VerblijfsKeuzeRepository verblijfsKeuzeRepository;

//    Hier komen alle methodes die iets te maken hebben met boekingen
    @RequestMapping("/reserveren")
    public String Reserveren(Model model) {

        ArrayList<VerblijfsKeuze> verblijfsKeuzes = verblijfsKeuzeRepository.getAllVerblijfsKeuzes();

        model.addAttribute("verblijfskeuzes", verblijfsKeuzes);

        return "layouts/boeking/reserveren";
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
