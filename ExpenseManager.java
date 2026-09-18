import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ExpenseManager.java
 *
 * Holds the in-memory list of expenses and contains all the
 * business logic: adding, deleting, searching, and summarizing.
 * Delegates file reading/writing to FileHandler so this class
 * only deals with expense-related operations.
 */
public class ExpenseManager {

    private final List<Expense> expenses;
    private final FileHandler fileHandler;
    private int nextId;

    public ExpenseManager(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.expenses = new ArrayList<>(fileHandler.loadExpenses());
        this.nextId = calculateNextId();
    }

    private int calculateNextId() {
        int max = 0;
        for (Expense e : expenses) {
            if (e.getId() > max) {
                max = e.getId();
            }
        }
        return max + 1;
    }

    /**
     * Adds a new expense after validating the supplied data.
     * Throws InvalidExpenseException if validation fails.
     */
    public Expense addExpense(String date, String category, String description, double amount)
            throws InvalidExpenseException {

        if (date == null || date.trim().isEmpty()) {
            throw new InvalidExpenseException("Date cannot be empty.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new InvalidExpenseException("Category cannot be empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidExpenseException("Description cannot be empty.");
        }
        if (amount <= 0) {
            throw new InvalidExpenseException("Amount must be a positive number.");
        }

        Expense expense = new Expense(nextId, date.trim(), category.trim(), description.trim(), amount);
        expenses.add(expense);
        nextId++;
        fileHandler.saveExpenses(expenses);
        return expense;
    }

    /**
     * Deletes an expense by its ID.
     * Returns true if an expense was found and removed, false otherwise.
     */
    public boolean deleteExpense(int id) {
        boolean removed = expenses.removeIf(e -> e.getId() == id);
        if (removed) {
            fileHandler.saveExpenses(expenses);
        }
        return removed;
    }

    /**
     * Returns all expenses in the order they were added.
     */
    public List<Expense> getAllExpenses() {
        return expenses;
    }

    /**
     * Returns expenses whose category matches the given text
     * (case-insensitive, partial match allowed).
     */
    public List<Expense> searchByCategory(String category) {
        List<Expense> result = new ArrayList<>();
        if (category == null) {
            return result;
        }
        String target = category.trim().toLowerCase();
        for (Expense e : expenses) {
            if (e.getCategory().toLowerCase().contains(target)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Calculates the sum of all recorded expenses.
     */
    public double getTotalExpenses() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        return total;
    }

    /**
     * Builds a category -> total amount summary.
     * Uses a LinkedHashMap to keep a predictable, insertion-based order.
     */
    public Map<String, Double> getCategorySummary() {
        Map<String, Double> summary = new LinkedHashMap<>();
        for (Expense e : expenses) {
            summary.merge(e.getCategory(), e.getAmount(), Double::sum);
        }
        return summary;
    }

    public boolean isEmpty() {
        return expenses.isEmpty();
    }
}
