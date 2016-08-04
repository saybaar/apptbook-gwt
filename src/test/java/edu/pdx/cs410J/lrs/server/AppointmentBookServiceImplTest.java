package edu.pdx.cs410J.lrs.server;

import com.google.gwt.junit.client.GWTTestCase;
import edu.pdx.cs410J.lrs.client.Appointment;
import edu.pdx.cs410J.lrs.client.AppointmentBook;
import edu.pdx.cs410J.lrs.client.ApptBookUtilities;
import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AppointmentBookServiceImplTest {

  @Test
  public void serviceReturnsExpectedAirline() {
    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();
    int numberOfAppointments = 6;
    AppointmentBook airline = service.createAppointmentBook("testOwner");
    for(int i = 0; i < numberOfAppointments; i++) {
      airline.addAppointment(new Appointment("appointment " + i, new Date(), new Date()));
    }
    assertThat(airline.getAppointments().size(), equalTo(numberOfAppointments));
  }

  @Test
  public void apptBookTest() {
    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();
    int numberOfAppointments = 6;
    AppointmentBook airline = service.createAppointmentBook("testOwner");
    for(int i = 0; i < numberOfAppointments; i++) {
      airline.addAppointment(new Appointment("appointment " + i, new Date(), new Date()));
    }
    assertThat(airline.getAppointments().size(), equalTo(numberOfAppointments));
  }

  @Test
  public void addAppointmentTest() {
    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();
    service.addAppointment("testOwner2", "testDescription", new Date(), new Date());
    assertThat(service.appointmentBookLibrary.get("testOwner2"), notNullValue());
    assertThat(service.getAppointmentBook("testOwner2").getAppointments().size(), equalTo(1));
  }

}
