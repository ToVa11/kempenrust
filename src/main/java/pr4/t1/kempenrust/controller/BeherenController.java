package pr4.t1.kempenrust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.DTO.UpdateReserveringDTO;
import pr4.t1.kempenrust.repository.BoekingDetailRepository;
import pr4.t1.kempenrust.repository.BoekingRepository;
import pr4.t1.kempenrust.repository.VerblijfsKeuzeRepository;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BeherenController {

    @Autowired
    BoekingDetailRepository boekingDetailRepository;
    @Autowired
    VerblijfsKeuzeRepository verblijfsKeuzeRepository;
    @Autowired
    BoekingRepository boekingRepository;

    //    Hier komen alle methodes in die iets te maken hebben met het beheren (CRUD) van het hotel
    @RequestMapping("/kamers")
    public String Kamers() {
    return "layouts/beheren/kamers";
}

    @RequestMapping("/arrangementen")
    public String Arrangementen() {
        return "layouts/beheren/arrangementen";
    }

    @RequestMapping("/reservering")
    public String Reservering(Model model, HttpServletRequest request) {
        BoekingDetail detail = boekingDetailRepository.getReservingByID(Integer.parseInt(request.getParameter("Id")));

        UpdateReserveringDTO updateReserveringDTO = new UpdateReserveringDTO();
        updateReserveringDTO.setDatumVan(detail.getBoeking().getDatumVan().toString());
        updateReserveringDTO.setDatumTot(detail.getBoeking().getDatumTot().toString());
        updateReserveringDTO.setVerblijfskeuzeID(detail.getBoeking().getVerblijfsKeuzeID());
        updateReserveringDTO.setAantalPersonen(detail.getBoeking().getAantalPersonen());
        updateReserveringDTO.setBoekingDetail(detail);
        updateReserveringDTO.setBoekingID(detail.getBoeking().getBoekingID());
        updateReserveringDTO.setVerblijfsKeuzes(verblijfsKeuzeRepository.getAllVerblijfsKeuzes());

        model.addAttribute("reservering", updateReserveringDTO);
        return "layouts/beheren/reservering";
    }

    @RequestMapping("/update/reservering")
    public String updateReservering(@ModelAttribute("reservering") UpdateReserveringDTO reservering, Model model) {
        int rows = boekingRepository.updateBoeking(reservering.getDatumVan(),reservering.getDatumTot(),reservering.getAantalPersonen(),reservering.getVerblijfskeuzeID(),reservering.getBoekingID());

        BoekingDetail detail = boekingDetailRepository.getReservingByID(reservering.getBoekingID());
        reservering.setBoekingDetail(detail);
        reservering.setVerblijfsKeuzes(verblijfsKeuzeRepository.getAllVerblijfsKeuzes());

        model.addAttribute("reservering", reservering);
        model.addAttribute("rows", rows);


        return "layouts/beheren/reservering";
    }
}
