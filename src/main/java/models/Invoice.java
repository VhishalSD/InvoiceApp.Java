package models;

/**
 * Modelklasse voor één factuur in de applicatie.
 * Bevat alle kerngegevens die nodig zijn om een factuur op te slaan:
 * datum, klantnaam, klantemail, omschrijving en het bedrag.
 * Wordt gebruikt door de services voor opslag, ophalen en verwerking.
 */
public class Invoice {
    private String date;
    private String customerName;
    private String customerEmail;
    private String description;
    private double amount;
    private double vatRate;
    private String invoiceNumber;
    private int quantity;
    private double pricePerUnit;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public double getTotalAmount() {
        double base = quantity * pricePerUnit;
        return base + (base * vatRate);
    }

    public Invoice(String date, String customerName, String customerEmail, String description, double amount, double vatRate, String invoiceNumber, int quantity, double pricePerUnit) {
        this.date = date;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.description = description;
        this.amount = amount;
        this.vatRate = vatRate;
        this.invoiceNumber = invoiceNumber;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }
}
