/**
 * InvalidExpenseException.java
 *
 * Custom checked exception thrown when expense data is invalid,
 * such as a negative amount, an empty required field, or a
 * corrupted record read from the data file.
 */
public class InvalidExpenseException extends Exception {

    public InvalidExpenseException(String message) {
        super(message);
    }
}
