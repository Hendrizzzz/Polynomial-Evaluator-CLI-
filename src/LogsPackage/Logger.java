package LogsPackage;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * The Logger saves the logs from the PolynomialEvaluator class' data.
 */
public class Logger {
    private static final ArrayList<Log> logs = new ArrayList<Log>();
    private static int logCountUponStartingProgram;


    /**
     * Adds a new log entry with date.
     *
     * @param entryLog be logged
     * @param date     the date and time of the log entry
     */
    public static void addLog(String entryLog, Date date) {
        Log newLog = new Log(entryLog, date);
        logs.add(newLog);
    }

    /**
     * Displays all log entries to the console.
     */
    public static void viewLog() {
        for (Log log : logs)
            System.out.println(log + "\n\n");
    }


    /**
     * Reads log data from the specified file and populates the logs list.
     * Each log entry consists of a date followed by the log content.
     */
    public static void ReadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/LogsPackage/Logs.txt"))) {
            String line;
            int lineCount = 1;
            StringBuilder logsList = new StringBuilder();
            Date date = null;

            while ((line = reader.readLine()) != null) {
                if (lineCount % 10 == 1) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
                    date = dateFormat.parse(line);
                }
                else if (lineCount % 10 == 0){
                    logsList.append(line).append("\n");
                    logs.add(new Log(logsList.toString(), date));
                    logsList.setLength(0); // clear the stringBuilder
                }
                else
                    logsList.append(line).append("\n");
                lineCount++;
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        logCountUponStartingProgram = logs.size();
    }


    /**
     * Appends and Saves any new log entries to the specified file.
     */
    public static void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/LogsPackage/Logs.txt", true))) {
            for (int i = logCountUponStartingProgram; i < logs.size(); i++) {
                writer.write(logs.get(i).toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
