package LogsPackage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    String logEntry;
    Date date;

    public Log(String logEntry, Date date) {
        this.logEntry = logEntry;
        this.date = date;
    }

    @Override
    public String toString() {
        // Create a SimpleDateFormat instance for the desired date and time format
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm a"); // Example: September 24, 2024 03:45 PM
        String formattedDateTime = formatter.format(date); // Format the date and time

        return formattedDateTime + "\n" + logEntry;
    }
}
