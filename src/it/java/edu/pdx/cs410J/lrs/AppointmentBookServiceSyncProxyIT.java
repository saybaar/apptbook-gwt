package edu.pdx.cs410J.lrs;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import edu.pdx.cs410J.lrs.client.Appointment;
import edu.pdx.cs410J.lrs.client.AppointmentBook;
import edu.pdx.cs410J.lrs.client.AppointmentBookService;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;

public class AppointmentBookServiceSyncProxyIT extends HttpRequestHelper {

  private final int httpPort = Integer.getInteger("http.port", 8888); //TODO had to change this from 8080
  private String webAppUrl = "http://localhost:" + httpPort + "/apptbook";

  @Test
  public void gwtWebApplicationIsRunning() throws IOException {
    Response response = get(this.webAppUrl);
    assertEquals(200, response.getCode());
  }

  @Test
  public void canInvokeAppointmentBookServiceWithGwtSyncProxy() {
    String moduleName = "apptbook"; //TODO is this right?
    SyncProxy.setBaseURL(this.webAppUrl + "/" + moduleName + "/");

    AppointmentBookService service = SyncProxy.createSync(AppointmentBookService.class);
    int numberOfAppointments = 5;
    AppointmentBook apptbook = service.createAppointmentBook("My Owner");
    assertEquals("My Owner", apptbook.getOwnerName());
    for (int i = 0; i < numberOfAppointments; i++) {
      apptbook.addAppointment(new Appointment("appt " + i, new Date(), new Date()));
    }
    assertEquals(numberOfAppointments, apptbook.getAppointments().size());
  }



}
