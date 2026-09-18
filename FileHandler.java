import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler.java
 *
 * Responsible for all file I/O: loading expenses from the CSV file
 * at startup and saving expenses back to it. Keeps file-handling
 * logic separate from business logic (ExpenseManager).
 */
public class FileHandler {

    private final String filePath;
    private static final String HEADER = "ID,Date,Category,Description,Amount";

    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads expenses from the CSV file. If the file does not exist,
     * it is created automatically with just a header row.
     * Malformed lines are skipped with a warning instead of crashing
     * the whole load process.
     */
    public List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        File file = new File(filePath);

        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                writeHeaderOnly();
                return expenses; // fresh file, nothing to load
            }
        } catch (IOException e) {
            System.out.println("Warning: could not create data file (" + e.getMessage() + ")");
            return expenses;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    if (line.trim().equalsIgnoreCase(HEADER)) {
                        continue; // skip header row
                    }
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    expenses.add(Expense.fromCSV(line));
                } catch (InvalidExpenseException e) {
                    System.out.println("Warning: skipping bad record -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading data file: " + e.getMessage());
        }

        return expenses;
    }

    /**
     * Overwrites the CSV file with the current in-memory list of expenses.
     * Called after every add/delete so the file always stays in sync.
     */
    public void saveExpenses(List<Expense> expenses) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(HEADER + System.lineSeparator());
            for (Expense e : expenses) {
                writer.write(e.toCSV() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error saving data file: " + e.getMessage());
        }
    }

    private void writeHeaderOnly() {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(HEADER + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Warning: could not write header (" + e.getMessage() + ")");
        }
    }
}
