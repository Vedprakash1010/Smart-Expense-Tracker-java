/**
 * Expense.java
 *
 * Represents a single expense record.
 * Demonstrates encapsulation (private fields with public getters/setters)
 * and basic OOP design.
 */
public class Expense {

    private int id;
    private String date;        // format: yyyy-MM-dd
    private String category;
    private String description;
    private double amount;

    public Expense(int id, String date, String category, String description, double amount) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    // ---------- Getters ----------
    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    // ---------- Setters ----------
    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Converts this expense into a single CSV line for file storage.
     * Commas inside text fields are replaced with semicolons to keep
     * the CSV format simple (no external CSV library is used).
     */
    public String toCSV() {
        return id + "," +
                date + "," +
                sanitize(category) + "," +
                sanitize(description) + "," +
                amount;
    }

    private String sanitize(String value) {
        return value.replace(",", ";");
    }

    /**
     * Builds an Expense object from a CSV line previously written by toCSV().
     * Throws InvalidExpenseException if the line is malformed.
     */
    public static Expense fromCSV(String line) throws InvalidExpenseException {
        String[] parts = line.split(",", -1);
        if (parts.length != 5) {
            throw new InvalidExpenseException("Corrupted record in file: " + line);
        }
        try {
            int id = Integer.parseInt(parts[0].trim());
            String date = parts[1].trim();
            String category = parts[2].trim();
            String description = parts[3].trim();
            double amount = Double.parseDouble(parts[4].trim());
            return new Expense(id, date, category, description, amount);
        } catch (NumberFormatException e) {
            throw new InvalidExpenseException("Invalid number format in record: " + line);
        }
    }

    /**
     * Formatted row used when printing the expense table to the console.
     */
    public String toTableRow() {
        return String.format("%-5d %-12s %-15s %-25s %10.2f",
                id, date, category, truncate(description, 25), amount);
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    @Override
    public String toString() {
        return toCSV();
    }
}
