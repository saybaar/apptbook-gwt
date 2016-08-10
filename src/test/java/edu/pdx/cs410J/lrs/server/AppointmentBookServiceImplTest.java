package edu.pdx.cs410J.lrs.server;

import com.google.gwt.junit.client.GWTTestCase;
import edu.pdx.cs410J.lrs.client.Appointment;
import edu.pdx.cs410J.lrs.client.AppointmentBook;
import edu.pdx.cs410J.lrs.client.ApptBookUtilities;
import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;

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

  @Test
  public void searchTest(){
    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();
    Date date1 = new Date(1321038660000L); //11/11/2011 11:11 AM
    Date date2 = new Date(1321039260000L); //11/11/2011 11:21 AM
    Date date3 = new Date(1321039860000L); //11/11/2011 11:21 AM
    service.addAppointment("testOwner1", "testDescription1", date1, date2);
    service.addAppointment("testOwner1", "testDescription2", date2, date3);
    AppointmentBook results = service.searchForAppointments("testOwner1", date1, date2);
    assertThat(results.getAppointments().size(), equalTo(1));
  }

  @Test
  public void getOwnersTest() {
    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();
    Set<String> s = service.getAllOwners();
    assertThat(s.size(), equalTo(0));
    service.createAppointmentBook("new1");
    s = service.getAllOwners();
    assertThat(s.size(), equalTo(1));
  }

  @Test
  public void testSearchResultsInLibrary() {
    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();
    Date date1 = new Date();
    Date date2 = new Date();
    service.createAppointmentBook("Owner1");
    service.addAppointment("Owner1", "desc", date1, date2);
    service.searchForAppointments("Owner1", date1, date2);
    assertThat(service.getAllOwners().size(), equalTo(1));
  }

}
