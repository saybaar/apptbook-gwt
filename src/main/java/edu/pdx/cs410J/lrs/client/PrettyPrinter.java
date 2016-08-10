package edu.pdx.cs410J.lrs.client;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class for dumping appointment book files
 */
public class PrettyPrinter {

    /**
     * Dumps an AppointmentBook to a pretty-printed HTML-ready String.
     * @param apptBook The appointment book to pretty-print.
     * @return The pretty-printed output as a String.
     */
    public static String dumpAllAppts(AbstractAppointmentBook apptBook) {
        StringBuilder sb = new StringBuilder();
        sb.append("Appointment book for " + apptBook.getOwnerName() + ":");
        for(Appointment appt : ((AppointmentBook) apptBook).getAppointments()) {
            sb.append("<br><br>");
            sb.append(dumpSingleAppt(appt));
        }
        return sb.toString();
    }

    /**
     * Dumps a single appointment in pretty format to an HTML-ready String.
     * @param appt Appointment to format
     * @return Pretty formatted appointment
     */
    public static String dumpSingleAppt(Appointment appt) {
        StringBuilder sb = new StringBuilder();
        sb.append(appt.getDescription() + "<br>\t\t");
        sb.append("    " + ApptBookUtilities.prettyDateTime(appt.getBeginTime()) + " to ");
        sb.append("    " + ApptBookUtilities.prettyDateTime(appt.getEndTime()) + "<br>");
        sb.append("  (" + appt.getDurationInMinutes() + " minutes)");
        return sb.toString();
    }

}
