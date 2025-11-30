package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Hulpfuncties voor het controleren en netjes maken van invoer.
 * Dit bestand zorgt ervoor dat alle data veilig, logisch en professioneel is
 * voordat het in de facturen wordt opgeslagen.
 * Alles wat je hier ziet wordt gebruikt om fouten te voorkomen.
 */
public class ValidationUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Controleert of een naam alleen geldige letters bevat
    // inclusief accenten en samengestelde achternamen.
    public static boolean isValidName(String name) {
        return name.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s\\-']+$");
    }

    // Zet elke naam in een nette schrijfwijze:
    // eerste letter hoofdletter, rest kleine letters.
    public static String formatName(String name) {
        String[] parts = name.toLowerCase().split(" ");
        StringBuilder formatted = new StringBuilder();

        for (String part : parts) {
            if (part.length() > 0) {
                formatted.append(part.substring(0, 1).toUpperCase())
                        .append(part.substring(1))
                        .append(" ");
            }
        }
        return formatted.toString().trim();
    }

    // Controleert of het e-mailadres de juiste structuur heeft.
    public static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // Bedrag moet groter dan 0 zijn en mag niet extreem hoog worden ingevuld.
    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount < 1_000_000;
    }

    // VAT (btw) moet tussen 0 en 1 liggen. Bijvoorbeeld 0.21 = 21%.
    public static boolean isValidVat(double vat) {
        return vat >= 0 && vat <= 1;
    }

    // Controleert of de datum bestaat in het formaat dd-MM-yyyy.
    // Gebruik van LocalDate zorgt ervoor dat onmogelijke data automatisch worden afgekeurd.
    public static boolean isValidDate(String date) {
        try {
            LocalDate parsed = LocalDate.parse(date, formatter);
            if (parsed.getYear() < 1950) {
                return false;
            }

            LocalDate today = LocalDate.now();
            if (parsed.isAfter(today)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
