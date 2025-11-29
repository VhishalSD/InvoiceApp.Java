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

    public Invoice(String date, String customerName, String customerEmail, String description, double amount) {
        this.date = date;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.description = description;
        this.amount = amount;
    }
}
