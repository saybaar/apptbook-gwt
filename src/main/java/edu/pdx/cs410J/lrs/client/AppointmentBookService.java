package edu.pdx.cs410J.lrs.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Date;

/**
 * A GWT remote service that returns a dummy appointment book
 */
@RemoteServiceRelativePath("appointments")
public interface AppointmentBookService extends RemoteService {

  /**
   * Returns the current date and time on the server
   * @param owner
   */
  public AppointmentBook createAppointmentBook(String owner);

  public AppointmentBook getAppointmentBook(String owner);

  //returns the updated apptbook
  public AppointmentBook addAppointment(String owner, String description, Date beginTime, Date endTime);

}
