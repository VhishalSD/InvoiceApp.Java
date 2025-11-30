package storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Invoice;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class InvoiceStorage {

    private static final String FILE_PATH = "src/main/resources/invoices.json";
    private Gson gson = new Gson();

    public void saveInvoices(ArrayList<Invoice> invoices) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(invoices, writer);
        } catch (Exception e) {
            System.out.println("Fout bij opslaan: " + e.getMessage());
        }
    }

    public ArrayList<Invoice> loadInvoices() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<Invoice>>() {}.getType();
            ArrayList<Invoice> loaded = gson.fromJson(reader, listType);

            // Als JSON bestaat maar null teruggeeft → leeg nieuw bestand maken
            if (loaded == null) {
                loaded = new ArrayList<>();
                saveInvoices(loaded);
            }

            return loaded;

        } catch (Exception e) {
            // Als bestand ontbreekt of beschadigd is → herstellen met lege lijst
            ArrayList<Invoice> empty = new ArrayList<>();
            saveInvoices(empty);
            return empty;
        }
    }
}