package edu.pdx.cs410J.lrs.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Collection;
import java.util.Date;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
  private final Alerter alerter;

  @VisibleForTesting
  Button addButton;
  TextBox ownerField;
  TextBox descriptionField;
  TextBox beginDateField;
  TextBox endDateField;

  Button deleteButton;
  FlexTable apptList;

  public AppointmentBookGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  @VisibleForTesting
  AppointmentBookGwt(Alerter alerter) {
    this.alerter = alerter;

    addWidgets();
  }

  private void addWidgets() {
    addButton = new Button("Add Appointments");
    addButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        createAppointment();
      }
    });

    this.ownerField = new TextBox();
    ownerField.setText("Owner");
    this.descriptionField = new TextBox();
    descriptionField.setText("Description");
    this.beginDateField = new TextBox();
    beginDateField.setText("Begin date/time");
    this.endDateField = new TextBox();
    endDateField.setText("End date/time");

    this.apptList = new FlexTable();
    apptList.insertRow(0);
    apptList.insertCell(0, 0);
    apptList.setHTML(0, 0, "Hello world!");
  }

  private Button getDeleteButton(int row, Collection<Appointment> appts) {
    final int i = row;
    final Collection<Appointment> apptsList = appts;
    deleteButton = new Button("x");
    deleteButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        apptList.removeRow(i);
        refreshTable(apptsList);
      }
    });
    return deleteButton;
  }

  private void refreshTable(Collection<Appointment> appts) {
    apptList.removeAllRows();
    int i = 0;
    for(Appointment appt : appts) {
      apptList.insertRow(i);
      apptList.insertCell(i, 0);
      apptList.insertCell(i, 1);
      apptList.setWidget(i, 0, getDeleteButton(i, appts));
      apptList.setHTML(i, 1, PrettyPrinter.dumpSingleAppt(appt));
      i++;
    }
  }

  private void createAppointment() {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    Date beginTime = null;
    Date endTime = null;
    try {
      beginTime = ApptBookUtilities.parseDateTime(beginDateField.getText());
      endTime = ApptBookUtilities.parseDateTime(endDateField.getText());
    } catch (IllegalArgumentException e) {
      alert(e);
      return;
    }
    async.addAppointment(ownerField.getText(), descriptionField.getText(), beginTime, endTime,
            new AsyncCallback<AppointmentBook>() {

      @Override
      public void onSuccess(AppointmentBook apptBook) {
        refreshTable(apptBook.getAppointments());
      }

      @Override
      public void onFailure(Throwable ex) {
        alert(ex);
      }
    });
  }

  private void displayInAlertDialog(AppointmentBook airline) {
    StringBuilder sb = new StringBuilder(airline.toString());
    sb.append("\n");

    Collection<Appointment> flights = airline.getAppointments();
    for (Appointment flight : flights) {
      sb.append(flight);
      sb.append("\n");
    }
    alerter.alert(sb.toString());
  }

  private void alert(Throwable ex) {
    alerter.alert(ex.toString());
  }

  @Override
  public void onModuleLoad() {
    RootPanel rootPanel = RootPanel.get();
    rootPanel.add(addButton);

    FlexTable addApptForm = new FlexTable();
    addApptForm.setWidget(0,0,new Label("Number of appointments"));
    addApptForm.setWidget(1,0,ownerField);
    addApptForm.setWidget(2,0,descriptionField);
    addApptForm.setWidget(3,0,beginDateField);
    addApptForm.setWidget(4,0,endDateField);

    DockPanel dockPanel = new DockPanel();
    dockPanel.add(addApptForm, DockPanel.WEST);
    dockPanel.add(apptList, DockPanel.EAST);

    rootPanel.add(addApptForm);
    rootPanel.add(dockPanel);
  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

}
