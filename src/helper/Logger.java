package helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * My own little logger. Meant to log all errors and special events.
 * <p>
 * To replace the various {@code System.out.println()} and
 * {@code System.err.println()}
 * <p>
 * By default every new log is on a new line
 *
 * @author Kareem Horstink
 * @version 0.7
 */
public class Logger {

    //Every string builder/buffer
    private static final StringBuffer ERROR = new StringBuffer();

    private static final StringBuffer LOGHIGH = new StringBuffer();

    private static final StringBuffer LOGLOW = new StringBuffer();

    private static final HashMap<String, StringBuffer> OTHER_LOG = new HashMap<>();

    /**
     * Log to your own log
     *
     * @param log Which log to log to. Note that low and high are reserved words
     * @param message The message to be logged
     */
    public static void LogAny(String log, String message) {
        if (log.matches(".*[Ll][Oo][Ww].*")) {
            LogLow(message);
        } else if (log.matches(".*[Hh][Ii][Gg][Hh].*")) {
            LogHigh(message);
        }
        if (OTHER_LOG.containsKey(log)) {
            OTHER_LOG.get(log).append('\n').append(message);
        } else {
            OTHER_LOG.put(log, new StringBuffer().append(message));
        }
    }

    /**
     * Get the current time. Meant for the logger
     *
     * @return Gets the current time
     */
    private static String getTime() {
        return new java.util.Date().toString();
    }

    private static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * To log a error. This version is meant if the sender is a static in which
     * case send {@code object.class.getName()}
     *
     * @param message What the error was
     * @param sender What object sent the error
     */
    public static void LogError(String message, String sender) {
        ERROR.append(sender).append('\n').append(getTime()).append('\n').append(message).append('\n');
    }

    /**
     * To log a error
     *
     * @param e The exception
     */
    public static void LogError(Exception e) {
        ERROR.append(e.getClass().getName()).append('\n').append(getTime()).append('\n').append(Arrays.toString(e.getStackTrace())).append('\n');
    }

    /**
     * To log a error
     *
     * @param message What the error was
     * @param o What object sent the error
     */
    public static void LogError(String message, Object o) {
        ERROR.append(o.getClass().getName()).append('\n').append(getTime()).append('\n').append(message).append('\n');
    }

    /**
     * To log a message of high importance
     *
     * @param message What you want to log
     */
    public static void LogHigh(String message) {
        LOGHIGH.append(getTime()).append('\n').append(message).append('\n');
    }

    /**
     * To log a message of low importance
     *
     * @param message What you want to log
     */
    public static void LogLow(String message) {
        LOGLOW.append(getTime()).append('\n').append(message).append('\n');
    }

    /**
     * Prints all the error that has been logged
     */
    public static void printError() {
        System.err.println("Error Log:");
        System.err.println(ERROR.toString());
    }

    /**
     * Prints all the logs that has been flagged as high importance
     */
    public static void printLogHigh() {
        System.out.println("High Importance Log:");
        System.out.println(LOGHIGH.toString());
    }

    /**
     * Prints all the logs that has been flagged as low importance
     */
    public static void printLogLow() {
        System.out.println("Low Importance Log:");
        System.out.println(LOGLOW.toString());
    }

    /**
     * Prints all the logs that has been flagged as an Error or High importance
     * or Low importance
     */
    public static void print() {
        printError();
        printLogHigh();
        printLogLow();
    }

    /**
     * Prints all the other logs
     * <p>
     * Not those that have flagged as an Error or High importance or Low
     * importance
     */
    public static void printOther() {
        OTHER_LOG.forEach((String t, StringBuffer u) -> {
            System.out.println("Log: " + t);
            System.out.println(u.toString());
        });
    }

    /**
     * Prints the specified log. Will log the the error if the log is not found
     *
     * @param log The log to print
     */
    public static void printOther(String log) {
        if (OTHER_LOG.containsKey(log)) {
            System.out.println(OTHER_LOG.get(log).toString());
        } else {
            Logger.LogError("Could not find logger - " + log, Logger.class.getName());
        }
    }

    /**
     * Saves all the log to a file
     * <p>
     * The file is saved current_date.log in a plain text manner
     *
     * Currently doesn't work right now
     */
    public static void saveAll() {
        StringBuilder tmp = new StringBuilder();
        tmp.append("Error Log:").append('\n').append(ERROR);
        tmp.append("--------------\n");
        tmp.append("High Importance Log: \n").append(LOGHIGH);
        tmp.append("--------------\n");
        tmp.append("Low Importance Log: \n").append(LOGLOW);
        OTHER_LOG.forEach((String t, StringBuffer u) -> {
            tmp.append("--------------\n");
            tmp.append("Log: ").append(t).append("\n");
            tmp.append(u.toString());
        });

    }

    /**
     * Exit the system with outputting most logs (Err, High and Low)
     */
    public static void exit() {
        print();
        System.exit(0);
    }
}
