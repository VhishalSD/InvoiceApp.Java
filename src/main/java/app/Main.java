package app;

import services.InvoiceService;
import java.util.Scanner;
import models.Invoice;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        InvoiceService service = new InvoiceService();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n--- Invoice Menu ---");
            System.out.println("1. Nieuwe factuur toevoegen");
            System.out.println("2. Factuur zoeken");
            System.out.println("3. Alle facturen tonen");
            System.out.println("4. Afsluiten");
            System.out.println("5. Factuur bewerken");
            System.out.println("6. Factuur verwijderen");
            System.out.print("Kies een optie: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Enter fix

            switch (choice) {
                case 1:
                    System.out.println("Factuur toevoegen ()");
                    System.out.print("Datum: ");
                    String date = scanner.nextLine();

                    System.out.print("Klantnaam: ");
                    String customerName = scanner.nextLine();

                    System.out.print("Klant email: ");
                    String customerEmail = scanner.nextLine();

                    System.out.print("Omschrijving: ");
                    String description = scanner.nextLine();

                    System.out.print("Bedrag: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); // Enter fix

                    System.out.print("VAT percentage (bijv. 0.21 voor 21%): ");
                    String vatInput = scanner.next();
                    double vat = Double.parseDouble(vatInput.replace(",", "."));
                    scanner.nextLine(); // Enter fix
                    Invoice invoice = new Invoice(date, customerName, customerEmail, description, amount, vat);
                    service.addInvoice(invoice);
                    System.out.println("Factuur succesvol toegevoegd!");
                    break;
                case 2:
                    System.out.println("Factuur zoeken ()");
                    System.out.print("Voer klantnaam in om te zoeken: ");
                    String searchName = scanner.nextLine();

                    Invoice found = service.searchInvoice(searchName);

                    if (found != null) {
                        System.out.println("Factuur gevonden:");
                        System.out.println("Datum: " + found.getDate());
                        System.out.println("Klantnaam: " + found.getCustomerName());
                        System.out.println("Klant email: " + found.getCustomerEmail());
                        System.out.println("Omschrijving: " + found.getDescription());
                        System.out.println("Bedrag: " + found.getAmount());
                        System.out.println("VAT: " + found.getVatRate());
                        System.out.println("Totaal incl. VAT: " + found.getTotalAmount());
                    } else {
                        System.out.println("Geen factuur gevonden voor deze klantnaam.");
                    }
                    break;
                case 3:
                    System.out.println("Alle facturen tonen ()");
                    ArrayList<Invoice> all = service.getAllInvoices();
                    if (all.isEmpty()) {
                        System.out.println("Geen facturen beschikbaar.");
                    } else {
                        for (Invoice inv : all) {
                            System.out.println("-------------------------");
                            System.out.println("Datum: " + inv.getDate());
                            System.out.println("Klantnaam: " + inv.getCustomerName());
                            System.out.println("Klant email: " + inv.getCustomerEmail());
                            System.out.println("Omschrijving: " + inv.getDescription());
                            System.out.println("Bedrag: " + inv.getAmount());
                            System.out.println("VAT: " + inv.getVatRate());
                            System.out.println("Totaal incl. VAT: " + inv.getTotalAmount());
                        }
                    }
                    break;
                case 4:
                    System.out.println("Programma afgesloten ()");
                    running = false;
                    break;
                case 5:
                    System.out.println("Factuur bewerken ()");
                    System.out.print("Voer klantnaam in van factuur die je wilt wijzigen: ");
                    String nameToEdit = scanner.nextLine();
                    Invoice toEdit = service.searchInvoice(nameToEdit);

                    if (toEdit != null) {

                        System.out.print("Nieuwe datum: ");
                        String newDate = scanner.nextLine();

                        System.out.print("Nieuwe omschrijving: ");
                        String newDescription = scanner.nextLine();

                        System.out.print("Nieuw bedrag: ");
                        double newAmount = scanner.nextDouble();
                        scanner.nextLine(); // Enter fix

                        System.out.print("Nieuwe VAT (bijv. 0.21): ");
                        double newVatRate = scanner.nextDouble();
                        scanner.nextLine(); // Enter fix

                        boolean updated = service.updateInvoice(nameToEdit, newDate, newDescription, newAmount);

                        if (updated) {
                            System.out.println("Factuur succesvol bijgewerkt!");
                        } else {
                            System.out.println("Er ging iets mis bij het bijwerken.");
                        }

                    } else {
                        System.out.println("Geen factuur gevonden met deze klantnaam.");
                    }

                    break;
                case 6:
                    System.out.println("Factuur verwijderen ()");
                    System.out.print("Voer klantnaam in van factuur die je wilt verwijderen: ");
                    String nameToDelete = scanner.nextLine();
                    boolean deleted = service.deleteInvoice(nameToDelete);
                    if (deleted) {
                        System.out.println("Factuur succesvol verwijderd!");
                    } else {
                        System.out.println("Geen factuur gevonden met deze klantnaam.");
                    }

                    break;
                default:
                    System.out.println("Ongeldige keuze, probeer opnieuw");
            }
        }

    }
}
