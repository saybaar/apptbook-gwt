package edu.pdx.cs410J.lrs.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;
import java.util.Set;

/**
 * The client-side interface to the ping service
 */
public interface AppointmentBookServiceAsync {

  /**
   * Create an appointment book on the server
   */
  void createAppointmentBook(String owner, AsyncCallback<AppointmentBook> async);

  /**
   * Get the appointment book for owner from the server
   * @param owner Owner's name
   * @param async Callback
     */
  void getAppointmentBook(String owner, AsyncCallback<AppointmentBook> async);

  /**
   * Add an appointment with the specified parameters to owner's appointment book
   * on the server
   * @param owner Owner's name
   * @param description Appointment description
   * @param beginTime Appointment start time
   * @param endTime Appointment end time
     * @param async Callback
     */
  void addAppointment(String owner, String description, Date beginTime, Date endTime,
                      AsyncCallback<AppointmentBook> async);

  /**
   * Get a temporary appointment book with a search of owner's appointments from the server
   * @param ownerName Owner's name
   * @param beginTime Search start time
   * @param endTime Search end time
   * @param asyncCallback Callback
     */
  void searchForAppointments(String ownerName, Date beginTime, Date endTime, AsyncCallback<AppointmentBook> asyncCallback);

  /**
   * Remove an appointment from owner's appointment book on the server
   * @param ownerName Owner's name
   * @param appt Appointment to remove
   * @param asyncCallback callback
     */
  void deleteAppointment(String ownerName, Appointment appt, AsyncCallback<AppointmentBook> asyncCallback);

  /**
   * Get a list of all the owners who have books stored on the server
   * @param asyncCallback Callback
     */
  void getAllOwners(AsyncCallback<Set<String>> asyncCallback);
}
