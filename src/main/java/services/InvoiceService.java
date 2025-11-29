package services;

import models.Invoice;
import java.util.ArrayList;


/**
 * Deze service beheert alle facturen in de applicatie.
 * Bewaart een lijst van Invoice-objecten en voert CRUD-acties uit.
 */
public class InvoiceService {

    private ArrayList<Invoice> invoices = new ArrayList<>();

    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
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

    public boolean updateInvoice(String customerName, String newDate, String newDescription, double newAmount) {
        for (Invoice invoice : invoices) {
            if (invoice.getCustomerName().equalsIgnoreCase(customerName)) {
                invoice.setDate(newDate);
                invoice.setDescription(newDescription);
                invoice.setAmount(newAmount);
                return true;
            }
        }
        return false;
    }

    public boolean deleteInvoice(String customerName) {
        for (Invoice invoice : invoices) {
            if (invoice.getCustomerName().equalsIgnoreCase(customerName)) {
                invoices.remove(invoice);
                return true;
            }
        }
        return false;
    }
}

