package edu.pdx.cs410J.lrs.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.*;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
  private final Alerter alerter;

  private AppointmentBook currentBook = null;

  @VisibleForTesting
  TextBox descriptionField;
  TextBox beginDateField;
  TextBox endDateField;
  Button addButton;

  TextBox searchBeginField;
  TextBox searchEndField;
  Button searchButton;

  ListBox bookSelector;
  Label listLabel;
  FlexTable apptList;

  Button newBookButton;
  TextBox ownerField;

  DockPanel masterPanel;

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

    this.ownerField = new TextBox();
    ownerField.setText("Owner");
    this.descriptionField = new TextBox();
    descriptionField.setText("Description");
    this.beginDateField = new TextBox();
    beginDateField.setText("11/11/1111 11:11 AM");
    this.endDateField = new TextBox();
    endDateField.setText("11/11/1111 11:21 AM");
    addButton = new Button("Add Appointment");
    addButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        createAppointment();
      }
    });

    this.searchBeginField = new TextBox();
    this.searchEndField = new TextBox();
    searchButton = new Button("Search");
    searchButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        searchForAppointments();
      }
    });

    this.listLabel = new Label();

    this.bookSelector = new ListBox();
    bookSelector.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent changeEvent) {
        if(bookSelector.getSelectedIndex() != 0) {
          changeActiveApptBook(bookSelector.getSelectedItemText());
        }
      }
    });

    this.newBookButton = new Button("Create book");
    newBookButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        createBook();
      }
    });

    this.apptList = new FlexTable();
    apptList.setBorderWidth(1);
    apptList.setCellPadding(5);

    updateUI();
  }

  private Button getDeleteButton(String ownerName, Appointment appointment) {
    final String owner = ownerName;
    final Appointment appt = appointment;
    Button deleteButton = new Button("x");
    deleteButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        deleteAppointment(owner, appt);
        updateUI();
      }
    });
    return deleteButton;
  }

  private void refreshTable(AppointmentBook apptBook) {
    apptList.removeAllRows();
    int i = 0;
    for(Appointment appt : apptBook.getAppointments()) {
      apptList.insertRow(i);
      apptList.insertCell(i, 0);
      apptList.insertCell(i, 1);
      apptList.setWidget(i, 0, getDeleteButton(apptBook.getOwnerName(), appt));
      apptList.setHTML(i, 1, PrettyPrinter.dumpSingleAppt(appt));
      i++;
    }
  }

  private void updateUI() {
    if(currentBook == null) {
      apptList.setVisible(false);
    } else if(currentBook.getAppointments().isEmpty()) {
      apptList.setVisible(true);
      apptList.removeAllRows();
      apptList.setHTML(0,0,"No appointments here!");
    } else {
      apptList.setVisible(true);
      refreshTable(currentBook);
    }

    if(currentBook == null) {
      addButton.setEnabled(false);
      searchButton.setEnabled(false);
      listLabel.setText("Select or create an appointment book to begin");
    } else {
      addButton.setEnabled(true);
      searchButton.setEnabled(true);
      listLabel.setText("Appointments for " + currentBook.getOwnerName());
    }

    populateBookMenu();
  }

  private void populateBookMenu() {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    async.getAllOwners(new AsyncCallback<Set<String>>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(Set<String> strings) {
        bookSelector.clear();
        bookSelector.insertItem("", 0);
        for(String string : strings) {
          bookSelector.addItem(string);
        }
        bookSelector.setSelectedIndex(0);
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

    FlexTable addApptForm = new FlexTable();
    addApptForm.setWidget(0,0,new Label("Add an appointment:"));
    addApptForm.setWidget(2,0,descriptionField);
    addApptForm.setWidget(3,0,beginDateField);
    addApptForm.setWidget(4,0,endDateField);
    addApptForm.setWidget(5,0,addButton);
    addApptForm.setWidth("400px");

    FlexTable searchForm = new FlexTable();
    searchForm.setWidget(0,0,new Label("Search for appointments between two dates:"));
    searchForm.setWidget(1,0,searchBeginField);
    searchForm.setWidget(2,0,searchEndField);
    searchForm.setWidget(3,0,searchButton);

    FlexTable newBookForm = new FlexTable();
    newBookForm.setWidget(0,0,new Label("Create a new appointment book:"));
    newBookForm.setWidget(1,0,ownerField);
    newBookForm.setWidget(2,0,newBookButton);

    FlexTable leftSide = new FlexTable();
    leftSide.setWidget(0,0,addApptForm);
    leftSide.setWidget(1,0,searchForm);
    leftSide.setWidget(2,0,newBookForm);
    leftSide.setCellPadding(10);

    FlexTable tableHeader = new FlexTable();
    tableHeader.setWidget(0,0,new Label("Select book: "));
    tableHeader.setWidget(0,1,bookSelector);

    FlexTable rightSide = new FlexTable();
    FlexTable label = new FlexTable();
    label.setWidget(0,0,listLabel);
    label.setBorderWidth(1);
    label.setCellPadding(10);
    rightSide.setWidget(0,0,tableHeader);
    rightSide.setWidget(1,0,label);
    rightSide.setWidget(2,0,apptList);
    rightSide.setCellPadding(10);

    masterPanel = new DockPanel();
    masterPanel.add(leftSide, DockPanel.WEST);
    masterPanel.add(rightSide, DockPanel.EAST);

    rootPanel.add(masterPanel);
  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

  private List<String> getDropDownContents() {
    List<String> results = new ArrayList<>();
    for(int i = 0; i < bookSelector.getItemCount(); i++) {
      results.add(bookSelector.getItemText(i));
    }
    return results;
  }

  private void createBook() {
    String owner = ownerField.getText();
    if(getDropDownContents().contains(owner)){
      alerter.alert("An appointment book for " + owner + " already exists!");
      return;
    }

    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);

    async.createAppointmentBook(owner, new AsyncCallback<AppointmentBook>() {
      @Override
      public void onSuccess(AppointmentBook apptBook) {
        currentBook = apptBook;
        bookSelector.addItem(apptBook.getOwnerName());
        updateUI();
      }
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

    });
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
    async.addAppointment(currentBook.getOwnerName(), descriptionField.getText(), beginTime, endTime,
            new AsyncCallback<AppointmentBook>() {

              @Override
              public void onSuccess(AppointmentBook apptBook) {
                currentBook = apptBook;
                updateUI();
              }

              @Override
              public void onFailure(Throwable ex) {
                alert(ex);
              }
            });
  }

  private void deleteAppointment(String owner, Appointment appt){
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    async.deleteAppointment(owner, appt, new AsyncCallback<AppointmentBook>(){

      @Override
      public void onSuccess(AppointmentBook appointmentBook) {
        currentBook = appointmentBook;
        updateUI();
      }

      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }
    });
  }

  private void searchForAppointments() {

    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    Date beginTime = null;
    Date endTime = null;
    try {
      beginTime = ApptBookUtilities.parseDateTime(searchBeginField.getText());
      endTime = ApptBookUtilities.parseDateTime(searchEndField.getText());
    } catch (IllegalArgumentException e) {
      alert(e);
      return;
    }
    final Date begin = beginTime;
    final Date end = endTime;
    async.searchForAppointments(currentBook.getOwnerName(), beginTime, endTime,
            new AsyncCallback<AppointmentBook>() {

              @Override
              public void onSuccess(AppointmentBook apptBook) {
                currentBook = apptBook;
                updateUI();
                listLabel.setText("Appointments for " + currentBook.getOwnerName() + " between " +
                        ApptBookUtilities.prettyDateTime(begin) + " and " +
                        ApptBookUtilities.prettyDateTime(end));
              }

              @Override
              public void onFailure(Throwable ex) {
                alert(ex);
              }
            });
  }

  private void changeActiveApptBook(String owner) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    async.getAppointmentBook(owner, new AsyncCallback<AppointmentBook>() {
      @Override
      public void onSuccess(AppointmentBook apptBook) {
        currentBook = apptBook;
        updateUI();
      }

      @Override
      public void onFailure(Throwable ex) {
        alert(ex);
      }
    });
  }

}
