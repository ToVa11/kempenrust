package pr4.t1.kempenrust.Helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class helper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String checkDatums(String datumAankomst, String datumVertrek) {
        LocalDate aankomst = LocalDate.parse(datumAankomst,formatter);
        LocalDate vertrek = LocalDate.parse(datumVertrek, formatter);
        String message=null;

        if(aankomst.isBefore(LocalDate.now()) ) {
            message = "Gelieve een datum in de toekomst te kiezen.";
        }
        else if(vertrek.isBefore(aankomst)) {
            message = "De vertrekdatum kan niet voor de aankomstdatum liggen.";
        }
        else if(vertrek.isEqual(aankomst)) {
            message = "De vertrekdatum en aankomstdatum kunnen niet op dezelfde dag liggen.";
        }

        return message;
    }
}
