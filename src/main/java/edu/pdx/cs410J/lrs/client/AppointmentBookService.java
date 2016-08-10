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
   * Creates a new appointment book on the server
   * @param owner Owner of the new book
   */
  public AppointmentBook createAppointmentBook(String owner);

  /**
   * Get the appointment book for owner from the server
   * @param owner Owner's name
   */
  public AppointmentBook getAppointmentBook(String owner);

  /**
   * Add an appointment with the specified parameters to owner's appointment book
   * on the server
   * @param owner Owner's name
   * @param description Appointment description
   * @param beginTime Appointment start time
   * @param endTime Appointment end time
   */
  public AppointmentBook addAppointment(String owner, String description, Date beginTime, Date endTime);

  /**
   * Get a temporary appointment book with a search of owner's appointments from the server
   * @param owner Owner's name
   * @param beginTime Search start time
   * @param endTime Search end time
   */
  public AppointmentBook searchForAppointments(String owner, Date beginTime, Date endTime);

  /**
   * Remove an appointment from owner's appointment book on the server
   * @param owner Owner's name
   * @param appt Appointment to remove
   */
  public AppointmentBook deleteAppointment(String owner, Appointment appt);

  /**
   * Get a list of all the owners who have books stored on the server
   */
  public Set<String> getAllOwners();

}
