package pr4.t1.kempenrust.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BoekingController {
//    Hier komen alle methodes die iets te maken hebben met boekingen
    @RequestMapping("/reserveren")
    public String Reserveren() {
        return "layouts/boeking/reserveren";
    }
}
