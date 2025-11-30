package app;

import services.InvoiceService;
import java.util.Scanner;
import models.Invoice;
import java.util.ArrayList;
import utils.ValidationUtils;

/**
 * Hoofdapplicatie van de InvoiceApp.
 * Deze klasse beheert het hoofdmenu, alle gebruikersinteracties
 * en stuurt de service-laag aan voor CRUD-acties op facturen.
 * Elke menu-optie is voorzien van validaties en duidelijke output,
 * zodat invoer betrouwbaar verwerkt wordt.
 */
public class Main {
    // ANSI kleurcodes voor professionele UI-uitvoer
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";
    public static void main(String[] args) {
        InvoiceService service = new InvoiceService();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        /**
         * Hoofdmenu-loop van de applicatie.
         * Deze loop blijft draaien zolang 'running' true is.
         * Elke iteratie toont het menu, leest de keuze van de gebruiker
         * en voert de bijbehorende functionaliteit uit.
         */
        while (running) {
            System.out.println("\n" + BLUE + BOLD + "--- INVOICE MENU ---" + RESET);
            System.out.println(CYAN + "1." + RESET + " Nieuwe factuur toevoegen");
            System.out.println(CYAN + "2." + RESET + " Factuur zoeken");
            System.out.println(CYAN + "3." + RESET + " Alle facturen tonen");
            System.out.println(CYAN + "4." + RESET + " Factuur bewerken");
            System.out.println(CYAN + "5." + RESET + " Factuur verwijderen");
            System.out.println(CYAN + "6." + RESET + " Sorteren");
            System.out.println(CYAN + "7." + RESET + " Afsluiten");
            System.out.print("Kies een optie: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Enter fix

            switch (choice) {
                /**
                 * CASE 1 — Nieuwe factuur toevoegen.
                 * De gebruiker voert datum, klantgegevens, omschrijving,
                 * bedrag en BTW in. Alle invoer wordt gevalideerd via ValidationUtils.
                 * Er wordt automatisch een uniek factuurnummer gegenereerd.
                 * Na toevoegen wordt de factuur opgeslagen in JSON via de service-laag.
                 */
                case 1:
                    System.out.println("Factuur toevoegen ()");
                    String date;
                    while (true) {
                        System.out.print("Datum (dd-MM-yyyy): ");
                        date = scanner.nextLine().trim();

                        if (date.isEmpty()) {
                            System.out.println(RED + "Datum mag niet leeg zijn." + RESET);
                            continue;
                        }

                        if (!ValidationUtils.isValidDate(date)) {
                            System.out.println(RED + "Ongeldige datum, probeer opnieuw." + RESET);
                            continue;
                        }

                        break;
                    }

                    String customerName;
                    while (true) {
                        System.out.print("Klantnaam: ");
                        customerName = scanner.nextLine();
                        if (ValidationUtils.isValidName(customerName)) {
                            customerName = ValidationUtils.formatName(customerName);
                            break;
                        }
                        System.out.println(RED + "Ongeldige naam, probeer opnieuw." + RESET);
                    }

                    String customerEmail;
                    while (true) {
                        System.out.print("Klant email: ");
                        customerEmail = scanner.nextLine();
                        if (ValidationUtils.isValidEmail(customerEmail)) break;
                        System.out.println(RED + "Ongeldige email, probeer opnieuw." + RESET);
                    }

                    String description;
                    while (true) {
                        System.out.print("Omschrijving: ");
                        description = scanner.nextLine();
                        if (!description.isBlank()) break;
                        System.out.println(RED + "Omschrijving mag niet leeg zijn." + RESET);
                    }



                    int quantity;
                    while (true) {
                        System.out.print("Aantal: ");
                        String qtyInput = scanner.nextLine().trim();
                        try {
                            quantity = Integer.parseInt(qtyInput);
                            if (quantity > 0) break;
                            System.out.println(RED + "Aantal moet hoger zijn dan 0." + RESET);
                        } catch (Exception e) {
                            System.out.println(RED + "Ongeldig aantal, voer een hele getal in." + RESET);
                        }
                    }

                    double pricePerUnit;
                    while (true) {
                        System.out.print("Prijs per stuk: ");
                        String ppuInput = scanner.nextLine().trim();
                        try {
                            pricePerUnit = Double.parseDouble(ppuInput.replace(",", "."));
                            if (pricePerUnit > 0) break;
                            System.out.println(RED + "Prijs per stuk mag geen 0 zijn." + RESET);
                        } catch (Exception e) {
                            System.out.println(RED + "Ongeldige prijs, voer een nummer in." + RESET);
                        }
                    }

                    double vat;
                    while (true) {
                        System.out.print("BTW percentage (bijv. 0.21): ");
                        String vatInput = scanner.nextLine().trim();

                        if (vatInput.isEmpty()) {
                            System.out.println(RED + "BTW mag niet leeg zijn." + RESET);
                            continue;
                        }

                        try {
                            vat = Double.parseDouble(vatInput.replace(",", "."));
                            if (ValidationUtils.isValidVat(vat)) break;

                            System.out.println(RED + "BTW moet tussen 0.01 en 0.50 liggen." + RESET);
                        } catch (Exception e) {
                            System.out.println(RED + "Ongeldige BTW waarde, voer een nummer in." + RESET);
                        }
                    }

                    String invoiceNumber = service.generateInvoiceNumber();
                    double amount = quantity * pricePerUnit;
                    Invoice invoice = new Invoice(date, customerName, customerEmail, description, amount, vat, invoiceNumber, quantity, pricePerUnit);
                    service.addInvoice(invoice);
                    System.out.println("Factuurnummer: " + invoiceNumber);
                    System.out.println(GREEN + "Factuur succesvol toegevoegd!" + RESET);
                    break;
                /**
                 * CASE 2 — Factuur zoeken.
                 * Submenu met twee opties:
                 * (1) zoeken op klantnaam (inclusief formattering en validatie)
                 * (2) zoeken op factuurnummer (validatie: mag niet leeg zijn)
                 * Toont alle factuurgegevens wanneer een match is gevonden.
                 */
                case 2:
                    boolean searching = true;
                    while (searching) {
                        System.out.println("\n" + BLUE + BOLD + "--- FACTUUR ZOEKEN ---" + RESET);
                        System.out.println("1. Zoeken op klantnaam");
                        System.out.println("2. Zoeken op factuurnummer");
                        System.out.println("3. Terug naar hoofdmenu");
                        System.out.print("Kies een optie: ");

                        int searchChoice = scanner.nextInt();
                        scanner.nextLine(); // Enter fix

                        if (searchChoice == 1) {
                            String searchName;
                            while (true) {
                                System.out.print("Voer klantnaam in om te zoeken: ");
                                searchName = scanner.nextLine();
                                if (ValidationUtils.isValidName(searchName)) {
                                    searchName = ValidationUtils.formatName(searchName);
                                    break;
                                }
                                System.out.println(RED + "Ongeldige naam, probeer opnieuw." + RESET);
                            }
                            Invoice found = service.searchInvoice(searchName);

                            if (found != null) {
                                System.out.println(CYAN + "┌────────────────────────────────┐" + RESET);
                                System.out.println(CYAN + "│ " + RESET + BOLD + "Factuurnummer: " + RESET + found.getInvoiceNumber());
                                System.out.println(CYAN + "│ " + RESET + "Datum: " + found.getDate());
                                System.out.println(CYAN + "│ " + RESET + "Klantnaam: " + found.getCustomerName());
                                System.out.println(CYAN + "│ " + RESET + "Klant email: " + found.getCustomerEmail());
                                System.out.println(CYAN + "│ " + RESET + "Omschrijving: " + found.getDescription());
                                System.out.println(CYAN + "│ " + RESET + "Aantal: " + found.getQuantity());
                                System.out.println(CYAN + "│ " + RESET + "Prijs per stuk: " + found.getPricePerUnit());
                                System.out.println(CYAN + "│ " + RESET + "Bedrag: " + found.getAmount());
                                System.out.println(CYAN + "│ " + RESET + "BTW: " + found.getVatRate());
                                System.out.println(CYAN + "│ " + RESET + "Totaal incl. BTW: " + found.getTotalAmount());
                                System.out.println(CYAN + "└────────────────────────────────┘" + RESET);
                            } else {
                                System.out.println(RED + "Geen factuur gevonden voor deze klantnaam." + RESET);
                            }

                        } else if (searchChoice == 2) {
                            String number;
                            while (true) {
                                System.out.print("Voer factuurnummer in: ");
                                number = scanner.nextLine();
                                if (!number.isBlank()) break;
                                System.out.println(RED + "Factuurnummer mag niet leeg zijn." + RESET);
                            }
                            Invoice found = service.searchInvoiceByNumber(number);

                            if (found != null) {
                                System.out.println(CYAN + "┌────────────────────────────────┐" + RESET);
                                System.out.println(CYAN + "│ " + RESET + BOLD + "Factuurnummer: " + RESET + found.getInvoiceNumber());
                                System.out.println(CYAN + "│ " + RESET + "Datum: " + found.getDate());
                                System.out.println(CYAN + "│ " + RESET + "Klantnaam: " + found.getCustomerName());
                                System.out.println(CYAN + "│ " + RESET + "Klant email: " + found.getCustomerEmail());
                                System.out.println(CYAN + "│ " + RESET + "Omschrijving: " + found.getDescription());
                                System.out.println(CYAN + "│ " + RESET + "Aantal: " + found.getQuantity());
                                System.out.println(CYAN + "│ " + RESET + "Prijs per stuk: " + found.getPricePerUnit());
                                System.out.println(CYAN + "│ " + RESET + "Bedrag: " + found.getAmount());
                                System.out.println(CYAN + "│ " + RESET + "BTW: " + found.getVatRate());
                                System.out.println(CYAN + "│ " + RESET + "Totaal incl. BTW: " + found.getTotalAmount());
                                System.out.println(CYAN + "└────────────────────────────────┘" + RESET);
                            } else {
                                System.out.println(RED + "Geen factuur gevonden met dit factuurnummer." + RESET);
                            }

                        } else if (searchChoice == 3) {
                            searching = false;

                        } else {
                            System.out.println(RED + "Ongeldige keuze, probeer opnieuw." + RESET);
                        }
                    }
                    break;
                /**
                 * CASE 3 — Alle facturen tonen.
                 * Haalt de volledige lijst op via de service-laag.
                 * Wanneer facturen bestaan, worden alle velden overzichtelijk weergegeven,
                 * inclusief factuurnummer en totaalbedrag met BTW.
                 */
                case 3:
                    System.out.println("\n" + BLUE + BOLD + "=== ALLE FACTUREN ===" + RESET);
                    ArrayList<Invoice> all = service.getAllInvoices();

                    if (all == null || all.isEmpty()) {
                        System.out.println(YELLOW + "Geen facturen beschikbaar." + RESET);
                        break;
                    }

                    int skipped = 0;

                    for (Invoice inv : all) {
                        if (inv == null ||
                            inv.getInvoiceNumber() == null ||
                            inv.getInvoiceNumber().isBlank() ||
                            inv.getDate() == null ||
                            inv.getDate().isBlank() ||
                            !ValidationUtils.isValidDate(inv.getDate()) ||
                            inv.getCustomerName() == null ||
                            inv.getCustomerName().isBlank() ||
                            !ValidationUtils.isValidName(inv.getCustomerName()) ||
                            inv.getCustomerEmail() == null ||
                            inv.getCustomerEmail().isBlank() ||
                            !ValidationUtils.isValidEmail(inv.getCustomerEmail()) ||
                            inv.getAmount() < 0 ||
                            !ValidationUtils.isValidAmount(inv.getAmount()) ||
                            !ValidationUtils.isValidVat(inv.getVatRate())) {

                            skipped++;
                            continue;
                        }

                        System.out.println(CYAN + "┌────────────────────────────────┐" + RESET);
                        System.out.println(CYAN + "│ " + RESET + BOLD + "Factuurnummer: " + RESET + inv.getInvoiceNumber());
                        System.out.println(CYAN + "│ " + RESET + "Datum: " + inv.getDate());
                        System.out.println(CYAN + "│ " + RESET + "Klantnaam: " + inv.getCustomerName());
                        System.out.println(CYAN + "│ " + RESET + "Klant email: " + inv.getCustomerEmail());
                        System.out.println(CYAN + "│ " + RESET + "Omschrijving: " + inv.getDescription());
                        System.out.println(CYAN + "│ " + RESET + "Aantal: " + inv.getQuantity());
                        System.out.println(CYAN + "│ " + RESET + "Prijs per stuk: " + inv.getPricePerUnit());
                        System.out.println(CYAN + "│ " + RESET + "Bedrag: " + inv.getAmount());
                        System.out.println(CYAN + "│ " + RESET + "BTW: " + inv.getVatRate());
                        System.out.println(CYAN + "│ " + RESET + "Totaal incl. BTW: " + inv.getTotalAmount());
                        System.out.println(CYAN + "└────────────────────────────────┘" + RESET);
                    }

                    if (skipped > 0) {
                        System.out.println("Let op: " + skipped + " factuur/facturen overgeslagen wegens ongeldige data.");
                    }
                    break;
                /**
                 * CASE 4 — Factuur bewerken.
                 * Submenu waarmee de gebruiker een factuur op factuurnummer kan opzoeken.
                 * Alle nieuwe waarden worden streng gevalideerd (datum, bedrag, BTW).
                 * Wanneer de factuur bestaat, worden de gegevens bijgewerkt en opgeslagen.
                 */
                case 4:
                    boolean editing = true;
                    while (editing) {
                        System.out.println("\n" + BLUE + BOLD + "=== FACTUUR BEWERKEN ===" + RESET);
                        System.out.println(CYAN + "1." + RESET + " Zoek en bewerk een factuur");
                        System.out.println(CYAN + "2." + RESET + " Terug naar hoofdmenu");
                        System.out.print("Kies een optie: ");

                        int editChoice = scanner.nextInt();
                        scanner.nextLine(); // Enter fix

                        if (editChoice == 1) {
                            String numberToEdit;
                            while (true) {
                                System.out.print("Voer factuurnummer in van factuur die je wilt wijzigen: ");
                                numberToEdit = scanner.nextLine();
                                if (!numberToEdit.isBlank()) break;
                                System.out.println(RED + "Factuurnummer mag niet leeg zijn." + RESET);
                            }
                            Invoice toEdit = service.searchInvoiceByNumber(numberToEdit);

                            if (toEdit != null) {
                                System.out.println(CYAN + "Huidig factuurnummer: " + RESET + BOLD + toEdit.getInvoiceNumber() + RESET);

                                String newDate;
                                while (true) {
                                    System.out.print("Nieuwe datum (dd-MM-yyyy): ");
                                    newDate = scanner.nextLine();
                                    if (ValidationUtils.isValidDate(newDate)) break;
                                    System.out.println(RED + "Ongeldige datum, probeer opnieuw." + RESET);
                                }

                                System.out.print("Nieuwe omschrijving: ");
                                String newDescription = scanner.nextLine();

                                int newQuantity;
                                while (true) {
                                    System.out.print("Nieuw aantal: ");
                                    String qtyInput = scanner.nextLine().trim();
                                    try {
                                        newQuantity = Integer.parseInt(qtyInput);
                                        if (newQuantity > 0) break;
                                        System.out.println(RED + "Aantal moet groter zijn dan 0." + RESET);
                                    } catch (Exception e) {
                                        System.out.println(RED + "Ongeldig aantal, voer een geheel getal in." + RESET);
                                    }
                                }

                                double newPricePerUnit;
                                while (true) {
                                    System.out.print("Nieuwe prijs per stuk: ");
                                    String ppuInput = scanner.nextLine().trim();
                                    try {
                                        newPricePerUnit = Double.parseDouble(ppuInput.replace(",", "."));
                                        if (newPricePerUnit > 0) break;
                                        System.out.println(RED + "Prijs per stuk moet groter zijn dan 0." + RESET);
                                    } catch (Exception e) {
                                        System.out.println(RED + "Ongeldige prijs, voer een nummer in." + RESET);
                                    }
                                }

                                double newAmount = newQuantity * newPricePerUnit;

                                double newVatRate;
                                while (true) {
                                    System.out.print("Nieuwe BTW (bijv. 0.21): ");
                                    String vatInput2 = scanner.next();
                                    scanner.nextLine();
                                    try {
                                        newVatRate = Double.parseDouble(vatInput2.replace(",", "."));
                                        if (ValidationUtils.isValidVat(newVatRate)) break;
                                    } catch (Exception ignored) {}
                                    System.out.println(RED + "Ongeldige BTW waarde, probeer opnieuw." + RESET);
                                }

                                boolean updated = service.updateInvoiceByNumber(numberToEdit, newDate, newDescription, newAmount, newVatRate, newQuantity, newPricePerUnit);

                                if (updated) {
                                    System.out.println(GREEN + "Factuur succesvol bijgewerkt!" + RESET);
                                    System.out.println(CYAN + "┌────────────────────────────────┐" + RESET);
                                    System.out.println(CYAN + "│ " + RESET + BOLD + "Factuurnummer: " + RESET + toEdit.getInvoiceNumber());
                                    System.out.println(CYAN + "│ " + RESET + "Datum: " + toEdit.getDate());
                                    System.out.println(CYAN + "│ " + RESET + "Klantnaam: " + toEdit.getCustomerName());
                                    System.out.println(CYAN + "│ " + RESET + "Klant email: " + toEdit.getCustomerEmail());
                                    System.out.println(CYAN + "│ " + RESET + "Omschrijving: " + toEdit.getDescription());
                                    System.out.println(CYAN + "│ " + RESET + "Aantal: " + toEdit.getQuantity());
                                    System.out.println(CYAN + "│ " + RESET + "Prijs per stuk: " + toEdit.getPricePerUnit());
                                    System.out.println(CYAN + "│ " + RESET + "Bedrag: " + toEdit.getAmount());
                                    System.out.println(CYAN + "│ " + RESET + "BTW: " + toEdit.getVatRate());
                                    System.out.println(CYAN + "│ " + RESET + "Totaal incl. BTW: " + toEdit.getTotalAmount());
                                    System.out.println(CYAN + "└────────────────────────────────┘" + RESET);
                                } else {
                                    System.out.println("Er ging iets mis bij het bijwerken.");
                                }

                            } else {
                                System.out.println(RED + "Geen factuur gevonden met dit factuurnummer." + RESET);
                                continue;
                            }

                        } else if (editChoice == 2) {
                            editing = false;
                        } else {
                            System.out.println(RED + "Ongeldige keuze, probeer opnieuw." + RESET);
                        }
                    }
                    break;
                /**
                 * CASE 5 — Factuur verwijderen.
                 * De gebruiker voert een factuurnummer in (validatie: niet leeg).
                 * Wanneer de factuur bestaat, wordt deze permanent verwijderd
                 * en wordt de JSON-opslag bijgewerkt.
                 */
                case 5:
                    System.out.println("\n" + BLUE + BOLD + "=== FACTUUR VERWIJDEREN ===" + RESET);
                    String numberToDelete;
                    while (true) {
                        System.out.print("Voer factuurnummer in van factuur die je wilt verwijderen: ");
                        numberToDelete = scanner.nextLine();
                        if (!numberToDelete.isBlank()) break;
                        System.out.println(RED + "Factuurnummer mag niet leeg zijn." + RESET);
                    }

                    Invoice checkDelete = service.searchInvoiceByNumber(numberToDelete);

                    if (checkDelete == null) {
                        System.out.println(RED + "Geen factuur gevonden met dit factuurnummer." + RESET);
                        break;
                    }
                    System.out.println(CYAN + "Te verwijderen factuur: " + RESET + BOLD + checkDelete.getInvoiceNumber() + RESET);

                    boolean deleted = service.deleteInvoiceByNumber(numberToDelete);

                    if (deleted) {
                        System.out.println(GREEN + "Factuur succesvol verwijderd!" + RESET);
                    } else {
                        System.out.println(RED + "Verwijderen is mislukt. Probeer het opnieuw." + RESET);
                    }

                    break;
                /**
                 * CASE 6 — Applicatie afsluiten.
                 * Zet 'running' op false waardoor het hoofdmenu stopt
                 * en de applicatie clean wordt beëindigd.
                 */
                /**
                 * CASE 6 — Facturen sorteren.
                 * Submenu waarmee de gebruiker facturen kan sorteren op datum,
                 * bedrag, klantnaam of factuurnummer.
                 */
                case 6:
                    boolean sorting = true;
                    while (sorting) {
                        System.out.println("\n" + BLUE + BOLD + "--- FACTUREN SORTEREN ---" + RESET);
                        System.out.println("1. Sorteren op datum");
                        System.out.println("2. Sorteren op bedrag");
                        System.out.println("3. Sorteren op klantnaam");
                        System.out.println("4. Sorteren op factuurnummer");
                        System.out.println("5. Terug naar hoofdmenu");
                        System.out.print("Kies een optie: ");

                        int sortChoice = scanner.nextInt();
                        scanner.nextLine(); // Enter fix

                        ArrayList<Invoice> sortedList = null;

                        switch (sortChoice) {
                            case 1:
                                sortedList = service.getInvoicesSortedByDate();
                                break;
                            case 2:
                                sortedList = service.getInvoicesSortedByAmount();
                                break;
                            case 3:
                                sortedList = service.getInvoicesSortedByName();
                                break;
                            case 4:
                                sortedList = service.getInvoicesSortedByNumber();
                                break;
                            case 5:
                                sorting = false;
                                continue;
                            default:
                                System.out.println(RED + "Ongeldige keuze, probeer opnieuw." + RESET);
                                continue;
                        }

                        if (sortedList == null || sortedList.isEmpty()) {
                            System.out.println(YELLOW + "Geen facturen beschikbaar om te sorteren." + RESET);
                            continue;
                        }

                        System.out.println("\n" + BLUE + BOLD + "=== GESORTEERDE FACTUREN ===" + RESET);
                        for (Invoice inv : sortedList) {
                            System.out.println(CYAN + "┌────────────────────────────────┐" + RESET);
                            System.out.println(CYAN + "│ " + RESET + BOLD + "Factuurnummer: " + RESET + inv.getInvoiceNumber());
                            System.out.println(CYAN + "│ " + RESET + "Datum: " + inv.getDate());
                            System.out.println(CYAN + "│ " + RESET + "Klantnaam: " + inv.getCustomerName());
                            System.out.println(CYAN + "│ " + RESET + "Klant email: " + inv.getCustomerEmail());
                            System.out.println(CYAN + "│ " + RESET + "Omschrijving: " + inv.getDescription());
                            System.out.println(CYAN + "│ " + RESET + "Aantal: " + inv.getQuantity());
                            System.out.println(CYAN + "│ " + RESET + "Prijs per stuk: " + inv.getPricePerUnit());
                            System.out.println(CYAN + "│ " + RESET + "Bedrag: " + inv.getAmount());
                            System.out.println(CYAN + "│ " + RESET + "BTW: " + inv.getVatRate());
                            System.out.println(CYAN + "│ " + RESET + "Totaal incl. BTW: " + inv.getTotalAmount());
                            System.out.println(CYAN + "└────────────────────────────────┘" + RESET);
                        }
                    }
                    break;
                case 7:
                    System.out.println("Programma afgesloten ()");
                    running = false;
                    break;
                default:
                    System.out.println(RED + "Ongeldige keuze, probeer opnieuw." + RESET);
            }
        }

    }
}
