package edu.pdx.cs410J.lrs.client;

import com.google.gwt.i18n.client.DateTimeFormat;

import java.text.ParseException;
import java.util.Date;

/**
 * Utility methods for the CS410J appointment book project
 */
public class ApptBookUtilities {

    //static final DateTimeFormat dateStorageFormat = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm aa");

    /**
     * Date/time parser used for both command line arguments and date/time strings read from file.
     * Parses a date from the machine-readable storage format.
     * @param dateTimeString A string date/time to check, expected format "MM/dd/yyyy HH:mm"
     * @return true if string is a valid date/time in "MM/dd/yyyy HH:mm" format, false otherwise
     * @throws IllegalArgumentException if date cannot be parsed
     */
    public static Date parseDateTime(String dateTimeString) throws IllegalArgumentException {
        DateTimeFormat dateStorageFormat = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm aa");
        //DateTimeFormat dateStorageFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);
        return dateStorageFormat.parseStrict(dateTimeString);
    }

    /**
     * Date/time dumper used to dump a date to the machine-readable storage format.
     * @param date The date object to dump to text
     * @return Formatted string that parseDateTime() can interpret
     */
    public static String dumpDateTime(Date date)  {
        DateTimeFormat dateStorageFormat = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
        return dateStorageFormat.format(date);
    }

    /**
     * Turns a Date into a String suitable for pretty-printing.
     * @param date The date to convert
     * @return A String of the date in a pleasant human-readable format
     */
    public static String prettyDateTime(Date date) {
        return DateTimeFormat.getFormat("MM/dd/yyyy (EEE), hh:mm a").format(date);
    }
}
