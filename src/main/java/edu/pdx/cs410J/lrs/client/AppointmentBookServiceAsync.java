package edu.pdx.cs410J.lrs.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

/**
 * The client-side interface to the ping service
 */
public interface AppointmentBookServiceAsync {

  /**
   * Return the current date/time on the server
   */
  void createAppointmentBook(String owner, AsyncCallback<AppointmentBook> async);

  void getAppointmentBook(String owner, AsyncCallback<AppointmentBook> async);

  void addAppointment(String owner, String description, Date beginTime, Date endTime,
                      AsyncCallback<AppointmentBook> async);

  void searchForAppointments(String ownerName, Date beginTime, Date endTime, AsyncCallback<AppointmentBook> asyncCallback);

  void deleteAppointment(String ownerName, int uid, AsyncCallback<AppointmentBook> asyncCallback);
}
