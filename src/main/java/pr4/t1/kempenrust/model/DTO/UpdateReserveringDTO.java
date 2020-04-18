package pr4.t1.kempenrust.model.DTO;

import pr4.t1.kempenrust.model.BoekingDetail;
import pr4.t1.kempenrust.model.Prijs;
import pr4.t1.kempenrust.model.VerblijfsKeuze;

import java.util.List;

public class UpdateReserveringDTO {

    private BoekingDetail reservering;
    private List<VerblijfsKeuze> verblijfsKeuzes;
    //private List<Prijs> prijsVrijeKamers;
    //private List<Prijs> prijsKamers;

    public BoekingDetail getReservering() {
        return reservering;
    }

    public void setReservering(BoekingDetail reservering) {
        this.reservering = reservering;
    }

    public List<VerblijfsKeuze> getVerblijfsKeuzes() {
        return verblijfsKeuzes;
    }

    public void setVerblijfsKeuzes(List<VerblijfsKeuze> verblijfsKeuzes) {
        this.verblijfsKeuzes = verblijfsKeuzes;
    }
}
