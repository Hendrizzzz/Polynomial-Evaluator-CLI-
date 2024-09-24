package LogsPackage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a log entry with a message and a timestamp.
 */
public class Log {
    String logEntry;
    Date date;

    /**
     * Constructs a Log object with the specified log entry and date.
     *
     * @param logEntry the message to be logged
     * @param date     the date and time of the log entry
     */
    public Log(String logEntry, Date date) {
        this.logEntry = logEntry;
        this.date = date;
    }


    /**
     * Returns a string representation of the log entry, including the formatted date and time.
     *
     * @return a formatted string containing the date and log entry
     */
    @Override
    public String toString() {
        // Create a SimpleDateFormat instance for the desired date and time format
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm a"); // Example: September 24, 2024 03:45 PM
        String formattedDateTime = formatter.format(date); // Format the date and time

        return formattedDateTime + "\n" + logEntry;
    }
}
