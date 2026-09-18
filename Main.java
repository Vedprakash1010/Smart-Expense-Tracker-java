import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main.java
 *
 * Entry point of the Smart Expense Tracker application.
 * Displays the console menu and routes user choices to the
 * appropriate ExpenseManager operations.
 */
public class Main {

    private static final String DATA_FILE = "data/expenses.csv";
    private static final Scanner scanner = new Scanner(System.in);
    private static ExpenseManager manager;

    public static void main(String[] args) {
        manager = new ExpenseManager(new FileHandler(DATA_FILE));

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readMenuChoice();

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    viewAllExpenses();
                    break;
                case 3:
                    searchByCategory();
                    break;
                case 4:
                    showTotalExpenses();
                    break;
                case 5:
                    showCategorySummary();
                    break;
                case 6:
                    deleteExpense();
                    break;
                case 7:
                    running = false;
                    System.out.println("\nThank you for using Smart Expense Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please enter a number between 1 and 7.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== SMART EXPENSE TRACKER =====");
        System.out.println("1. Add Expense");
        System.out.println("2. View All Expenses");
        System.out.println("3. Search by Category");
        System.out.println("4. Show Total Expenses");
        System.out.println("5. Category-wise Summary");
        System.out.println("6. Delete Expense");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Reads the menu choice safely. Returns -1 (an invalid option)
     * instead of crashing if the user types non-numeric input.
     */
    private static int readMenuChoice() {
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ---------- Menu actions ----------

    private static void addExpense() {
        System.out.println("\n--- Add New Expense ---");

        System.out.print("Enter date (yyyy-MM-dd): ");
        String date = scanner.nextLine().trim();

        System.out.print("Enter category (e.g., Food, Travel, Bills): ");
        String category = scanner.nextLine().trim();

        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Enter amount: ");
        String amountInput = scanner.nextLine().trim();

        double amount;
        try {
            amount = Double.parseDouble(amountInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a numeric value. Expense not added.");
            return;
        }

        try {
            Expense expense = manager.addExpense(date, category, description, amount);
            System.out.println("Expense added successfully with ID: " + expense.getId());
        } catch (InvalidExpenseException e) {
            System.out.println("Could not add expense: " + e.getMessage());
        }
    }

    private static void viewAllExpenses() {
        System.out.println("\n--- All Expenses ---");
        List<Expense> expenses = manager.getAllExpenses();

        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        printTableHeader();
        for (Expense e : expenses) {
            System.out.println(e.toTableRow());
        }
    }

    private static void searchByCategory() {
        System.out.print("\nEnter category to search: ");
        String category = scanner.nextLine().trim();

        if (category.isEmpty()) {
            System.out.println("Category cannot be empty.");
            return;
        }

        List<Expense> results = manager.searchByCategory(category);
        if (results.isEmpty()) {
            System.out.println("No expenses found in category matching \"" + category + "\".");
            return;
        }

        System.out.println("\n--- Expenses in category matching \"" + category + "\" ---");
        printTableHeader();
        for (Expense e : results) {
            System.out.println(e.toTableRow());
        }
    }

    private static void showTotalExpenses() {
        double total = manager.getTotalExpenses();
        System.out.printf("%nTotal expenses recorded: %.2f%n", total);
    }

    private static void showCategorySummary() {
        System.out.println("\n--- Category-wise Spending Summary ---");
        Map<String, Double> summary = manager.getCategorySummary();

        if (summary.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        System.out.printf("%-20s %15s%n", "Category", "Total Amount");
        System.out.println("----------------------------------------");
        for (Map.Entry<String, Double> entry : summary.entrySet()) {
            System.out.printf("%-20s %15.2f%n", entry.getKey(), entry.getValue());
        }
    }

    private static void deleteExpense() {
        if (manager.isEmpty()) {
            System.out.println("\nNo expenses to delete.");
            return;
        }

        System.out.print("\nEnter the Expense ID to delete: ");
        String input = scanner.nextLine().trim();

        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a whole number.");
            return;
        }

        boolean deleted = manager.deleteExpense(id);
        if (deleted) {
            System.out.println("Expense with ID " + id + " deleted successfully.");
        } else {
            System.out.println("No expense found with ID " + id + ".");
        }
    }

    private static void printTableHeader() {
        System.out.printf("%-5s %-12s %-15s %-25s %10s%n",
                "ID", "Date", "Category", "Description", "Amount");
        System.out.println("---------------------------------------------------------------------");
    }
}
