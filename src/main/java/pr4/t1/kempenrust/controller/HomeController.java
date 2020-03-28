package pr4.t1.kempenrust.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
//      Hier komen de rest van de methodes (standaard)
    @RequestMapping("/")
    public String Index() {
        return "layouts/home/index";
    }
}
