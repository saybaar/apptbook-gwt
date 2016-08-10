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

  @Override
  public AppointmentBook createAppointmentBook(String owner) {
    AppointmentBook apptBook = new AppointmentBook(owner);
    appointmentBookLibrary.put(owner, apptBook);
    return apptBook;
  }

  @Override
  public AppointmentBook getAppointmentBook(String owner) {
    return appointmentBookLibrary.get(owner);
  }

  @Override
  public AppointmentBook addAppointment(String owner, String description, Date beginTime, Date endTime) {
    AppointmentBook apptBook = appointmentBookLibrary.get(owner);
    if(apptBook == null) {
      apptBook = createAppointmentBook(owner);
    }
    apptBook.addAppointment(new Appointment(description, beginTime, endTime));
    return apptBook;
  }

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

  //Won't return keySet() as is because it's synchronized with the map.
  public Set<String> getAllOwners() {
    Set<String> results = new HashSet<>();
    for(String s : appointmentBookLibrary.keySet()) {
      results.add(s);
    }
    return results;
  }

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}
