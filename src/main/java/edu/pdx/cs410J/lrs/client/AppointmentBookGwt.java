package edu.pdx.cs410J.lrs.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.*;

/**
 * Main GWT class for appointment book managements
 */
public class AppointmentBookGwt implements EntryPoint {
  private final Alerter alerter;

  private AppointmentBook currentBook = null;

  private final HTML readme = new HTML("Lydia Simmons - Advanced Java Project 5<br><br>" +
          "This webapp supports multiple appointment books. Use the form at the bottom left<br>" +
          "to create them. Use the \"select book\" dropdown menu to switch between books <br>" +
          "and return from search results. <br><br>" +
          "The \"print\" button will display the current appointment book in a plain-text <br>" +
          "format suitable for copying to another document or printing with the system dialog. <br><br>" +
          "Use the forms on the left to create and search appointments in the currently <br>" +
          "displayed appointment book. Appointments may be deleted with the \"x\" button.<br><br><br>" +
          "Click anywhere to close this message.");

  private MenuBar mainMenu;
  private PopupPanel readmePanel;

  private TextBox descriptionField;
  private TextBox beginDateField;
  private TextBox endDateField;
  private Button addButton;

  private TextBox searchBeginDateField;
  private TextBox searchEndDateField;
  private Button searchButton;

  private ListBox bookSelector;
  private Label listLabel;
  private Button printButton;
  private PopupPanel apptsPanel;
  private Button closeApptsPanelButton;
  private FlexTable apptList;

  private Button newBookButton;
  private TextBox ownerField;

  public AppointmentBookGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  private AppointmentBookGwt(Alerter alerter) {
    this.alerter = alerter;

    addWidgets();
  }

  /**
   * Initializes widgets and sets their behavior.
   */
  private void addWidgets() {

    this.mainMenu = new MenuBar();
    MenuBar helpMenu = new MenuBar(true);
    Command showReadme = new Command() {
      @Override
      public void execute() {
        readmePanel.show();
      }
    };
    helpMenu.addItem(new MenuItem("Readme", showReadme));
    mainMenu.addItem("Help", helpMenu);
    readmePanel = new PopupPanel(true);
    String windowWidth = Window.getClientWidth() + "px";
    String windowHeight = Window.getClientHeight() + "px";
    readmePanel.setSize(windowWidth, windowHeight);
    readmePanel.setWidget(readme);
    readme.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        readmePanel.hide();
      }
    });

    this.descriptionField = new TextBox();
    descriptionField.setText("Description");
    this.beginDateField = new TextBox();
    beginDateField.setText("08/10/2016 12:00 PM");
    this.endDateField = new TextBox();
    endDateField.setText("08/10/2016 01:00 PM");
    addButton = new Button("Add Appointment");
    addButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        createAppointment();
      }
    });

    this.searchBeginDateField = new TextBox();
    searchBeginDateField.setText("08/10/2016 12:00 PM");
    this.searchEndDateField = new TextBox();
    searchEndDateField.setText("08/10/2016 01:00 PM");
    searchButton = new Button("Search");
    searchButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        searchForAppointments();
      }
    });

    this.listLabel = new Label();
    this.printButton = new Button("Print");
    apptsPanel = new PopupPanel(true);
    apptsPanel.setSize(windowWidth, windowHeight);
    closeApptsPanelButton = new Button("Close");
    closeApptsPanelButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        apptsPanel.hide();
      }
    });
    printButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        HTML apptsText = new HTML(PrettyPrinter.dumpAllAppts(currentBook));
        FlexTable printScreenContents = new FlexTable();
        printScreenContents.setWidget(0,0,apptsText);
        printScreenContents.setWidget(1,0,closeApptsPanelButton);
        apptsPanel.setWidget(printScreenContents);
        apptsPanel.show();
      }
    });


    this.bookSelector = new ListBox();
    bookSelector.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent changeEvent) {
        if(bookSelector.getSelectedIndex() != 0) {
          changeActiveApptBook(bookSelector.getSelectedItemText());
        }
      }
    });

    this.ownerField = new TextBox();
    ownerField.setText("Owner");
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

  /**
   * Returns a delete button keyed to this owner and appointment
   * @param ownerName Owner of the appointment
   * @param appointment Appointment to delete
   * @return Button that will delete the appointment
     */
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

  /**
   * Redraws the appointment list table with the appointments in apptBook
   * @param apptBook Appointment book to list appointments from
     */
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

  /**
   * Refreshes the UI: disables/enables appropriate buttons, sets label
   * text, refreshes the appointment table and the book select menu
   */
  private void updateUI() {
    if(currentBook == null) {
      apptList.setVisible(false);
      printButton.setVisible(false);

    } else if(currentBook.getAppointments().isEmpty()) {
      apptList.setVisible(true);
      apptList.removeAllRows();
      apptList.setHTML(0,0,"No appointments here!");
      printButton.setVisible(true);
    } else {
      apptList.setVisible(true);
      printButton.setVisible(true);
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

  /**
   * Populates the book select menu with all the owners stored on the server
   */
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

  /**
   * Utility method to alert the user of an exception
   * @param ex Exception to display
     */
  private void alert(Throwable ex) {
    alerter.alert(ex.toString());
  }

  /**
   * Arranges widgets and sets some immutable labels
   */
  @Override
  public void onModuleLoad() {
    RootPanel rootPanel = RootPanel.get();

    FlexTable addApptForm = new FlexTable();
    addApptForm.setWidget(0,0,new Label("Add an appointment with a start and end date:"));
    addApptForm.setWidget(1,0,descriptionField);
    addApptForm.setWidget(3,0,beginDateField);
    addApptForm.setWidget(5,0,endDateField);
    addApptForm.setWidget(6,0,addButton);
    addApptForm.setWidth("400px");

    FlexTable searchForm = new FlexTable();
    searchForm.setWidget(0,0,new Label("Search for appointments between two dates:"));
    searchForm.setWidget(2,0,searchBeginDateField);
    searchForm.setWidget(4,0,searchEndDateField);
    searchForm.setWidget(5,0,searchButton);

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
    label.setWidget(0,1,printButton);
    label.setBorderWidth(1);
    label.setCellPadding(10);
    rightSide.setWidget(0,0,tableHeader);
    rightSide.setWidget(1,0,label);
    rightSide.setWidget(2,0,apptList);
    rightSide.setCellPadding(10);

    DockPanel masterPanel = new DockPanel();
    masterPanel.add(mainMenu, DockPanel.NORTH);
    masterPanel.add(leftSide, DockPanel.WEST);
    masterPanel.add(rightSide, DockPanel.EAST);

    rootPanel.add(masterPanel);
  }

  /**
   * Interface for an Alerter object
   */
  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

  /**
   * Gets the contents of the dropdown menu, which correspond to existing owners
   * @return List of owners
     */
  private List<String> getDropDownContents() {
    List<String> results = new ArrayList<>();
    for(int i = 0; i < bookSelector.getItemCount(); i++) {
      results.add(bookSelector.getItemText(i));
    }
    return results;
  }

  /**
   * Creates a new appointment book on the server and switches to it in the client
   */
  private void createBook() {
    String owner = ownerField.getText();
    if(owner.equals("")) {
      alerter.alert("Owner name must not be empty.");
      return;
    }
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

  /**
   * Creates a new appointment on the server for the currently viewed book,
   * based on text fields
   */
  private void createAppointment() {
    Date beginTime;
    Date endTime;
    try {
      beginTime = ApptBookUtilities.parseDateTime(beginDateField.getText());
    } catch (IllegalArgumentException e) {
      alerter.alert("Malformatted date/time: " + beginDateField.getText() +
            "\nExpected - mm/dd/yyyy hh:mm xm");
      return;
    }
    try {
      endTime = ApptBookUtilities.parseDateTime(endDateField.getText());
    } catch (IllegalArgumentException e) {alerter.alert("Malformatted date/time: " + endDateField.getText() +
            "\nExpected - mm/dd/yyyy hh:mm xm");
      return;
    }
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
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

  /**
   * Deletes the specified appointment from the owner's appointment book
   * @param owner Owner of the book to modify
   * @param appt Appointment to delete
     */
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

  /**
   * Searches for appointments in the currently viewed book, based on text fields,
   * and displays the results
   */
  private void searchForAppointments() {

    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    Date beginTime;
    Date endTime;
    try {
      beginTime = ApptBookUtilities.parseDateTime(searchBeginDateField.getText());
    } catch (IllegalArgumentException e) {
      alerter.alert("Malformatted date/time: " + searchBeginDateField.getText() +
              "\nExpected - mm/dd/yyyy hh:mm xm");
      return;
    }
    try {
      endTime = ApptBookUtilities.parseDateTime(searchEndDateField.getText());
    } catch (IllegalArgumentException e) {alerter.alert("Malformatted date/time: " + searchEndDateField.getText() +
            "\nExpected - mm/dd/yyyy hh:mm xm");
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

  /**
   * Changes the currently viewed appointment book to owner's
   * @param owner Owner of the book to display
     */
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
