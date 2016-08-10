package edu.pdx.cs410J.lrs.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

  public AppointmentBook addAppointment(String owner, String description, Date beginTime, Date endTime);

  public AppointmentBook searchForAppointments(String owner, Date beginTime, Date endTime);

  public AppointmentBook deleteAppointment(String owner, Appointment appt);

  public Set<String> getAllOwners();

}
