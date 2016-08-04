package edu.pdx.cs410J.lrs.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.lrs.client.Appointment;
import edu.pdx.cs410J.lrs.client.AppointmentBook;
import edu.pdx.cs410J.lrs.client.AppointmentBookService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The server-side implementation of the division service
 */
public class AppointmentBookServiceImpl extends RemoteServiceServlet implements AppointmentBookService
{

  Map<String, AppointmentBook> appointmentBookLibrary = new HashMap<>();

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
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}
