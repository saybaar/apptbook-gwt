package edu.pdx.cs410J.lrs.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import edu.pdx.cs410J.AbstractAppointment;

import java.util.Date;

/**
 * Class for Appointment objects.
 */
public class Appointment extends AbstractAppointment implements Comparable<Appointment> {
  private String description;
  private Date startDateTime;
  private Date endDateTime;

  /**
   * Creates a new Appointment with the given string parameters.
   * @param description A description of the appointment
   * @param startDateTime Start date/time as a Date object
   * @param endDateTime End date/time as a Date object
     */
  public Appointment(String description, Date startDateTime, Date endDateTime) {
    this.description = description;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
  }
  public Appointment() {
    this.description = "test description";
    this.startDateTime = new Date();
    this.endDateTime = new Date();
  }

  /**
   * Returns the start date/time as a Date.
   * @return the start date/time as a Date
     */
  @Override
  public Date getBeginTime() {
    return startDateTime;
  }

  /**
   * Returns the end date/time as a Date.
   * @return the end date/time as a Date
   */
  @Override
  public Date getEndTime() {
    return endDateTime;
  }

  /**
   * Returns a string with the beginning date and time
   * @return string with the beginning date and time
     */
  @Override
  public String getBeginTimeString() {
    return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(startDateTime);
  }

  /**
   * Returns a string with the ending date and time
   * @return string with the ending date and time
     */
  @Override
  public String getEndTimeString() {
    return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(endDateTime);
  }

  /**
   * Returns the appointment's description
   * @return the appointment's description
     */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Compares to another appointment, sorting by start time, then end time, then description.
   * @param other The appointment to compare to
   * @return Relative ordering of the two appointments
     */
  @Override
  public int compareTo(Appointment other) {
    int startDateOrder = this.startDateTime.compareTo(other.startDateTime);
    int endDateOrder = this.endDateTime.compareTo(other.endDateTime);
    int descriptionOrder = this.description.compareTo(other.description);
    if(startDateOrder != 0) {
      return startDateOrder;
    } else if(endDateOrder != 0) {
      return endDateOrder;
    } else if(descriptionOrder != 0) {
      return descriptionOrder;
    }
    return 0;
  }

  /**
   * Calculates this appointment's duration in minutes.
   * @return Duration in minutes, as a long
     */
  public long getDurationInMinutes() {
    return((endDateTime.getTime() - startDateTime.getTime()) / (1000*60)) % 60;
  }
}
