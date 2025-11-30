package services;

import models.Invoice;
import java.util.ArrayList;
import storage.InvoiceStorage;


/**
 * Deze service beheert alle facturen in de applicatie.
 * Bewaart een lijst van Invoice-objecten en voert CRUD-acties uit.
 */
public class InvoiceService {

    private final InvoiceStorage storage = new InvoiceStorage();
    private ArrayList<Invoice> invoices;
    private int nextInvoiceId = 1;

    public InvoiceService() {
        this.invoices = storage.loadInvoices();
        if (this.invoices == null) this.invoices = new ArrayList<>();
        for (Invoice inv : this.invoices) {
            String num = inv.getInvoiceNumber();
            if (num != null && num.contains("-")) {
                String[] parts = num.split("-");
                try {
                    int parsed = Integer.parseInt(parts[1]);
                    if (parsed >= nextInvoiceId) nextInvoiceId = parsed + 1;
                } catch (Exception ignored) {}
            }
        }
    }

    public void addInvoice(Invoice invoice) {
        if (invoice.getInvoiceNumber() == null || invoice.getInvoiceNumber().isEmpty()) {
            invoice.setInvoiceNumber(generateInvoiceNumber());
            nextInvoiceId++;
        }
        invoices.add(invoice);
        storage.saveInvoices(invoices);
    }

    public ArrayList<Invoice> getAllInvoices() {
        return invoices;
    }

    public Invoice searchInvoice(String customerName) {

        for (Invoice invoice : invoices) {
            if (invoice.getCustomerName().equalsIgnoreCase(customerName)) {
                return invoice;
            }
        }
        return null;
    }

    public Invoice searchInvoiceByNumber(String invoiceNumber) {
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceNumber() != null &&
                invoice.getInvoiceNumber().equalsIgnoreCase(invoiceNumber)) {
                return invoice;
            }
        }
        return null;
    }

    public boolean updateInvoice(String customerName, String newDate, String newDescription, double newAmount, double newVatRate, int newQuantity, double newPricePerUnit) {
        for (Invoice invoice : invoices) {
            if (invoice.getCustomerName().equalsIgnoreCase(customerName)) {
                invoice.setDate(newDate);
                invoice.setDescription(newDescription);
                invoice.setAmount(newAmount);
                invoice.setVatRate(newVatRate);
                invoice.setQuantity(newQuantity);
                invoice.setPricePerUnit(newPricePerUnit);
                storage.saveInvoices(invoices);
                return true;
            }
        }
        return false;
    }

    public boolean updateInvoiceByNumber(String invoiceNumber, String newDate, String newDescription, double newAmount, double newVatRate, int newQuantity, double newPricePerUnit) {
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceNumber() != null &&
                invoice.getInvoiceNumber().equalsIgnoreCase(invoiceNumber)) {

                invoice.setDate(newDate);
                invoice.setDescription(newDescription);
                invoice.setAmount(newAmount);
                invoice.setVatRate(newVatRate);
                invoice.setQuantity(newQuantity);
                invoice.setPricePerUnit(newPricePerUnit);
                storage.saveInvoices(invoices);
                return true;
            }
        }
        return false;
    }

    public boolean deleteInvoice(String customerName) {
        for (Invoice invoice : invoices) {
            if (invoice.getCustomerName().equalsIgnoreCase(customerName)) {
                invoices.remove(invoice);
                storage.saveInvoices(invoices);
                return true;
            }
        }
        return false;
    }

    public boolean deleteInvoiceByNumber(String invoiceNumber) {
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceNumber() != null &&
                invoice.getInvoiceNumber().equalsIgnoreCase(invoiceNumber)) {

                invoices.remove(invoice);
                storage.saveInvoices(invoices);
                return true;
            }
        }
        return false;
    }

    public String generateInvoiceNumber() {
        int year = java.time.LocalDate.now().getYear();
        String padded = String.format("%04d", nextInvoiceId);
        return year + "-" + padded;
    }

    /**
     * Geeft een gesorteerde lijst op datum (oud → nieuw).
     */
    public ArrayList<Invoice> getInvoicesSortedByDate() {
        ArrayList<Invoice> fresh = storage.loadInvoices();
        fresh.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        return fresh;
    }

    /**
     * Geeft een gesorteerde lijst op bedrag (laag → hoog).
     */
    public ArrayList<Invoice> getInvoicesSortedByAmount() {
        ArrayList<Invoice> fresh = storage.loadInvoices();
        fresh.sort((a, b) -> Double.compare(a.getAmount(), b.getAmount()));
        return fresh;
    }

    /**
     * Geeft een gesorteerde lijst op klantnaam (A → Z).
     */
    public ArrayList<Invoice> getInvoicesSortedByName() {
        ArrayList<Invoice> fresh = storage.loadInvoices();
        fresh.sort((a, b) -> a.getCustomerName().compareToIgnoreCase(b.getCustomerName()));
        return fresh;
    }

    /**
     * Geeft een gesorteerde lijst op factuurnummer (2025-0001 → 2025-9999).
     */
    public ArrayList<Invoice> getInvoicesSortedByNumber() {
        ArrayList<Invoice> fresh = storage.loadInvoices();
        fresh.sort((a, b) -> a.getInvoiceNumber().compareToIgnoreCase(b.getInvoiceNumber()));
        return fresh;
    }
}
