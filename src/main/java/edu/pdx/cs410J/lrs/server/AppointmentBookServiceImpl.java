package edu.pdx.cs410J.lrs.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.lrs.client.Appointment;
import edu.pdx.cs410J.lrs.client.AppointmentBook;
import edu.pdx.cs410J.lrs.client.AppointmentBookService;

import java.util.*;

/**
 * The server-side implementation of the division service
 */
public class AppointmentBookServiceImpl extends RemoteServiceServlet implements AppointmentBookService
{

  Map<String, AppointmentBook> appointmentBookLibrary = new HashMap();

  /**
   * Creates a new appointment book and stores it in the library
   * @param owner Owner of the new book
   * @return the new book
     */
  @Override
  public AppointmentBook createAppointmentBook(String owner) {
    AppointmentBook apptBook = new AppointmentBook(owner);
    appointmentBookLibrary.put(owner, apptBook);
    return apptBook;
  }

  /**
   * Gets owner's appointment book from the library
   * @param owner Owner's name
   * @return Owner's book
     */
  @Override
  public AppointmentBook getAppointmentBook(String owner) {
    return appointmentBookLibrary.get(owner);
  }

  /**
   * Adds an appointment to owner's book
   * @param owner Owner's name
   * @param description Appointment description
   * @param beginTime Appointment start time
   * @param endTime Appointment end time
     * @return The modified appointment book
     */
  @Override
  public AppointmentBook addAppointment(String owner, String description, Date beginTime, Date endTime) {
    AppointmentBook apptBook = appointmentBookLibrary.get(owner);
    if(apptBook == null) {
      apptBook = createAppointmentBook(owner);
    }
    apptBook.addAppointment(new Appointment(description, beginTime, endTime));
    return apptBook;
  }

  /**
   * Searches for appointments in owner's book between two times
   * @param owner Owner's name
   * @param beginTime Search start time
   * @param endTime Search end time
     * @return An appointment book containing search results, which is not stored
   * in the library
     */
  @Override
  public AppointmentBook searchForAppointments(String owner, Date beginTime, Date endTime) {
    AppointmentBook searchInBook = appointmentBookLibrary.get(owner);
    AppointmentBook results = new AppointmentBook(owner);
    for(Appointment appt : searchInBook.getAppointments()) {
      if(appt.getBeginTime().compareTo(beginTime) >= 0 &&
              appt.getEndTime().compareTo(endTime) <= 0) {
        results.addAppointment(appt);
      }
    }
    return results;
  }

  /**
   * Deletes an appointment from owner's book
   * @param owner Owner's name
   * @param appt Appointment to remove
   * @return Modified book
     */
  public AppointmentBook deleteAppointment(String owner, Appointment appt){
    AppointmentBook apptBook = appointmentBookLibrary.get(owner);
    /*
    for(Appointment appt : apptBook.getAppointments()) {
      if(appt.getUid() == uid) {
        apptBook.getAppointments().remove(appt);
      }
    }*/
    apptBook.getAppointments().remove(appt);
    return apptBook;
  }

  /**
   * Gets the set of owners who have books in the library. (Can't just use
   * keySet() because it's synchronized with the map and will not serialize.)
   * @return Set of owners
     */
  public Set<String> getAllOwners() {
    Set<String> results = new HashSet<>();
    for(String s : appointmentBookLibrary.keySet()) {
      results.add(s);
    }
    return results;
  }

  /**
   * Handler for unexpected failures
   * @param unhandled something that happened!
     */
  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}
