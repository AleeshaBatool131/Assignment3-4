package com.example.hspsm;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.*;

import java.io.*;

import static com.example.hspsm.Admin.analyzePlotStatistics;
import static com.example.hspsm.Admin.generateReports;

public class HelloApplication extends Application{
    public static int userCount = 1;
    @Override
    public void start(Stage stage) throws IOException {
        welcomeScreen(stage);
        stage.show();
    }
    public void welcomeScreen(Stage stage){

        VBox vBox = new VBox();
        Text welcome = new Text("Welcome to Housing Society Plot Management System");
        welcome.setTextAlignment(TextAlignment.CENTER);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(welcome);
        Scene scene = new Scene(vBox, 800, 800);
        stage.setScene(scene);
        stage.setTitle("Welcome Screen");
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(e -> loginScreen(stage)); // Transition to loginScreen after pause
        pause.play();
    }
    public static void loginScreen(Stage stage){
        VBox vBox = new VBox();
        Label usernameLabel = new Label("Username: ");
        Label passwordLabel = new Label("Password: ");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button login = new Button("Login");
        List<User> users = loadUsers();
        Text invalidMessage = new Text();
        invalidMessage.setTextAlignment(TextAlignment.CENTER);
        invalidMessage.setFill(Color.RED);
            login.setOnAction(e->{
                boolean isValidUser = false;
                if(users!=null)
                {
                    for(User user: users){
                        if(user.getUsername().equals(usernameField.getText())&&user.getPassword().equals(passwordField.getText())){
                            isValidUser = true;
                            break;
                        }
                    }
                }
                if (isValidUser) {
                    buyerDashboard(stage); // Proceed to buyer dashboard if valid
                } else {
                    invalidMessage.setText("Invalid Username or Password");
                }
            });

        vBox.setSpacing(5);
        vBox.getChildren().addAll(usernameLabel, usernameField, passwordLabel,passwordField);
        Button admin = new Button("Login as Admin");
        Button register = new Button("Signup");
        register.setOnAction(e->{
            registerUser(stage);
        });
        admin.setOnAction(e->{
            adminLoginScreen(stage);
        });
        vBox.getChildren().addAll(invalidMessage,login ,admin, register);
        Scene scene = new Scene(vBox, 800, 800);
        stage.setScene(scene);
        stage.setTitle("Login Screen");
    }

    public static void adminDashboardScene(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        vBox.getChildren().add(titleLabel);

        // Buttons for different functionalities
        Button manageUsersButton = new Button("Manage Users");
        Button managePlotsButton = new Button("Manage Plots");
        Button managePaymentsButton = new Button("Manage Payments");
        Button generateReportsButton = new Button("Generate Reports");
        Button systemSettingsButton = new Button("System Settings");
        Button logoutButton = new Button("Logout");

        // Add buttons to the VBox
        vBox.getChildren().addAll(manageUsersButton, managePlotsButton, managePaymentsButton,
                generateReportsButton, systemSettingsButton, logoutButton);

        // Button action handlers
        manageUsersButton.setOnAction(e -> {
            UserManagementScene(stage);

        });

        managePlotsButton.setOnAction(e -> {
            // Implement plot management functionality here
            managePlotsScene(stage);
        });

        managePaymentsButton.setOnAction(e -> {
            // Implement payment management functionality here
           managePaymentsScene(stage);
        });

        generateReportsButton.setOnAction(e -> {
            // Implement report generation functionality here
            generateReportScene(stage);
        });

        logoutButton.setOnAction(e -> {
            // Implement logout functionality, e.g., return to login screen
            loginScreen(stage);
        });

        Scene scene = new Scene(vBox, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
    }


    public static void UserManagementScene(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Label titleLabel = new Label("User Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        vBox.getChildren().add(titleLabel);

        // Table to display users
        TableView<User> userTable = new TableView<>();
        userTable.setItems(loadUsers()); // Load users into the table

        TableColumn<User, String> idColumn = new TableColumn<>("User ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        userTable.getColumns().addAll(idColumn, usernameColumn, roleColumn, emailColumn, phoneColumn);

        // Buttons for user management actions
        Button addButton = new Button("Add User");
        Button editButton = new Button("Edit User");
        Button deleteButton = new Button("Delete User");

        // Add user functionality
        addButton.setOnAction(e -> {
            Stage addStage = new Stage();
            VBox addVBox = new VBox(10);
            addVBox.setAlignment(Pos.CENTER);

            TextField usernameField = new TextField();
            usernameField.setPromptText("Username");

            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Password");

            TextField roleField = new TextField();
            roleField.setPromptText("Role (Admin/Buyer)");

            TextField emailField = new TextField();
            emailField.setPromptText("Email");

            TextField phoneField = new TextField();
            phoneField.setPromptText("Phone Number");

            Button saveButton = new Button("Save");
            saveButton.setOnAction(event -> {
                ObservableList<User> users = userTable.getItems();
                users.add(new User(usernameField.getText(), passwordField.getText(), roleField.getText(), emailField.getText(), phoneField.getText()));
                saveUsers(users);
                addStage.close();
            });

            addVBox.getChildren().addAll(usernameField, passwordField, roleField, emailField, phoneField, saveButton);
            addStage.setScene(new Scene(addVBox, 300, 400));
            addStage.setTitle("Add User");
            addStage.show();
        });

        // Edit user functionality
        editButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                Stage editStage = new Stage();
                VBox editVBox = new VBox(10);
                editVBox.setAlignment(Pos.CENTER);

                TextField emailField = new TextField(selectedUser.getEmail());
                emailField.setPromptText("Email");

                TextField phoneField = new TextField(selectedUser.getPhoneNumber());
                phoneField.setPromptText("Phone Number");

                TextField roleField = new TextField(selectedUser.getRole());
                roleField.setPromptText("Role (Admin/Buyer)");

                Button saveButton = new Button("Save Changes");
                saveButton.setOnAction(event -> {
                    selectedUser.setEmail(emailField.getText());
                    selectedUser.setPhoneNumber(phoneField.getText());
                    selectedUser.setRole(roleField.getText());
                    userTable.refresh(); // Refresh table to reflect changes
                    saveUsers(userTable.getItems());
                    editStage.close();
                });

                editVBox.getChildren().addAll(emailField, phoneField, roleField, saveButton);
                editStage.setScene(new Scene(editVBox, 300, 300));
                editStage.setTitle("Edit User");
                editStage.show();
            }
        });

        // Delete user functionality
        deleteButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                userTable.getItems().remove(selectedUser);
                saveUsers(userTable.getItems());
            }
        });

        // Back button to return to admin dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> adminDashboardScene(stage));

        // Layout adjustments
        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(userTable, buttonBox, backButton);

        Scene scene = new Scene(vBox, 800, 600);
        stage.setScene(scene);
        stage.setTitle("User Management");
    }

    public static void managePlotsScene(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Label titleLabel = new Label("Plot Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        vBox.getChildren().add(titleLabel);

        // Table to display plots
        TableView<Plot> plotTable = new TableView<>();
        plotTable.setItems(loadPlots()); // Load plots into the table

        TableColumn<Plot, Integer> idColumn = new TableColumn<>("Plot ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("plotId"));

        TableColumn<Plot, String> numberColumn = new TableColumn<>("Plot Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("plotNumber"));

        TableColumn<Plot, Double> lengthColumn = new TableColumn<>("Length");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn<Plot, Double> widthColumn = new TableColumn<>("Width");
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));

        TableColumn<Plot, Double> areaColumn = new TableColumn<>("Total Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("totalArea"));

        TableColumn<Plot, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Plot, String> gpsColumn = new TableColumn<>("GPS Coordinates");
        gpsColumn.setCellValueFactory(new PropertyValueFactory<>("gpsCoordinates"));

        TableColumn<Plot, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Plot, Double> priceUnitColumn = new TableColumn<>("Price Per Unit");
        priceUnitColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));

        TableColumn<Plot, Double> priceColumn = new TableColumn<>("Total Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        TableColumn<Plot, String> developmentColumn = new TableColumn<>("Development Status");
        developmentColumn.setCellValueFactory(new PropertyValueFactory<>("developmentStatus"));

        plotTable.getColumns().addAll(idColumn, numberColumn, lengthColumn, widthColumn, areaColumn, locationColumn, gpsColumn, statusColumn, priceUnitColumn, priceColumn, developmentColumn);

        // Buttons for plot management actions
        Button addButton = new Button("Add Plot");
        Button editButton = new Button("Edit Plot");
        Button deleteButton = new Button("Delete Plot");

        // Add plot functionality
        addButton.setOnAction(e -> {
            Stage addStage = new Stage();
            VBox addVBox = new VBox(10);
            addVBox.setAlignment(Pos.CENTER);

            TextField numberField = new TextField();
            numberField.setPromptText("Plot Number");

            TextField lengthField = new TextField();
            lengthField.setPromptText("Length");

            TextField widthField = new TextField();
            widthField.setPromptText("Width");

            TextField locationField = new TextField();
            locationField.setPromptText("Location");

            TextField gpsField = new TextField();
            gpsField.setPromptText("GPS Coordinates");

            TextField statusField = new TextField();
            statusField.setPromptText("Status");

            TextField priceUnitField = new TextField();
            priceUnitField.setPromptText("Price Per Unit");

            TextField developmentField = new TextField();
            developmentField.setPromptText("Development Status");

            Button saveButton = new Button("Save");
            saveButton.setOnAction(event -> {
                try {
                    int newId = plotTable.getItems().size() + 1;
                    double length = Double.parseDouble(lengthField.getText());
                    double width = Double.parseDouble(widthField.getText());
                    double pricePerUnit = Double.parseDouble(priceUnitField.getText());

                    Plot newPlot = new Plot(newId, numberField.getText(), length, width, locationField.getText(), gpsField.getText(), statusField.getText(), pricePerUnit, developmentField.getText());
                    ObservableList<Plot> plots = plotTable.getItems();
                    plots.add(newPlot);
                    savePlots(plots);
                    addStage.close();
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input values.");
                }
            });

            addVBox.getChildren().addAll(numberField, lengthField, widthField, locationField, gpsField, statusField, priceUnitField, developmentField, saveButton);
            addStage.setScene(new Scene(addVBox, 400, 500));
            addStage.setTitle("Add Plot");
            addStage.show();
        });

        // Edit plot functionality
        editButton.setOnAction(e -> {
            Plot selectedPlot = plotTable.getSelectionModel().getSelectedItem();
            if (selectedPlot != null) {
                Stage editStage = new Stage();
                VBox editVBox = new VBox(10);
                editVBox.setAlignment(Pos.CENTER);

                TextField numberField = new TextField(selectedPlot.getPlotNumber());
                numberField.setPromptText("Plot Number");

                TextField lengthField = new TextField(String.valueOf(selectedPlot.getLength()));
                lengthField.setPromptText("Length");

                TextField widthField = new TextField(String.valueOf(selectedPlot.getWidth()));
                widthField.setPromptText("Width");

                TextField locationField = new TextField(selectedPlot.getLocation());
                locationField.setPromptText("Location");

                TextField gpsField = new TextField(selectedPlot.getGpsCoordinates());
                gpsField.setPromptText("GPS Coordinates");

                TextField statusField = new TextField(selectedPlot.getStatus());
                statusField.setPromptText("Status");

                TextField priceUnitField = new TextField(String.valueOf(selectedPlot.getPricePerUnit()));
                priceUnitField.setPromptText("Price Per Unit");

                TextField developmentField = new TextField(selectedPlot.getDevelopmentStatus());
                developmentField.setPromptText("Development Status");

                Button saveButton = new Button("Save Changes");
                saveButton.setOnAction(event -> {
                    try {
                        selectedPlot.setPlotNumber(numberField.getText());
                        selectedPlot.setLength(Double.parseDouble(lengthField.getText()));
                        selectedPlot.setWidth(Double.parseDouble(widthField.getText()));
                        selectedPlot.setLocation(locationField.getText());
                        selectedPlot.setGpsCoordinates(gpsField.getText());
                        selectedPlot.setStatus(statusField.getText());
                        selectedPlot.setPricePerUnit(Double.parseDouble(priceUnitField.getText()));
                        selectedPlot.setTotalArea(selectedPlot.getLength() * selectedPlot.getWidth());
                        selectedPlot.setTotalPrice(selectedPlot.getTotalArea() * selectedPlot.getPricePerUnit());
                        selectedPlot.setDevelopmentStatus(developmentField.getText());

                        plotTable.refresh();
                        savePlots(plotTable.getItems());
                        editStage.close();
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid input values.");
                    }
                });

                editVBox.getChildren().addAll(numberField, lengthField, widthField, locationField, gpsField, statusField, priceUnitField, developmentField, saveButton);
                editStage.setScene(new Scene(editVBox, 400, 500));
                editStage.setTitle("Edit Plot");
                editStage.show();
            }
        });

        // Delete plot functionality
        deleteButton.setOnAction(e -> {
            Plot selectedPlot = plotTable.getSelectionModel().getSelectedItem();
            if (selectedPlot != null) {
                plotTable.getItems().remove(selectedPlot);
                savePlots(plotTable.getItems());
            }
        });

        // Back button to return to admin dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> adminDashboardScene(stage));

        // Layout adjustments
        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(plotTable, buttonBox, backButton);

        Scene scene = new Scene(vBox, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Plot Management");
    }

    public static void managePaymentsScene(Stage stage){
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Display all payments
        ListView<Payment> paymentListView = new ListView<>();
        paymentListView.getItems().setAll(loadPayments());  // Assuming loadPayments() returns the list of payments

        // Button to add a new payment
        Button addPaymentButton = new Button("Add Payment");
        addPaymentButton.setOnAction(e -> {
            // Open a new scene to add payment details (you can implement a dialog or new scene here)
            System.out.println("Adding a new payment...");
        });

        // Button to remove a selected payment
        Button removePaymentButton = new Button("Remove Payment");
        removePaymentButton.setOnAction(e -> {
            Payment selectedPayment = paymentListView.getSelectionModel().getSelectedItem();
            if (selectedPayment != null) {
                ObservableList<Payment> payments = loadPayments();
                payments.remove(selectedPayment);
                savePayments(payments);
                paymentListView.getItems().setAll(loadPayments()); // Refresh the list view
                System.out.println("Payment removed: " + selectedPayment);
            } else {
                System.out.println("Please select a payment to remove.");
            }
        });

        // Button to update payment details
        Button updatePaymentButton = new Button("Update Payment");
        updatePaymentButton.setOnAction(e -> {
            Payment selectedPayment = paymentListView.getSelectionModel().getSelectedItem();
            if (selectedPayment != null) {
                // Open a new scene or dialog to update payment details
                System.out.println("Updating payment: " + selectedPayment);
            } else {
                System.out.println("Please select a payment to update.");
            }
        });

        // Add components to layout
        layout.getChildren().addAll(paymentListView, addPaymentButton, removePaymentButton, updatePaymentButton);

        // Create and show the scene
        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Manage Payments");
        stage.show();
    }
    public static void generateReportScene(Stage stage){
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Generate the report (can be based on the Analytics class or Admin methods)
        String report = generateReports();  // Assuming generateReports() generates a report string
        String plotStatistics = analyzePlotStatistics();  // Assuming analyzePlotStatistics() returns plot statistics

        // Display the reports in TextAreas or Labels
        final TextArea reportTextArea = new TextArea(report + "\n\n" + plotStatistics);
        reportTextArea.setEditable(false);  // Make the report area read-only
        reportTextArea.setWrapText(true);

        // Button to print the report (or generate a PDF if needed)
        Button printButton = new Button("Print Report");
        reportTextArea.setEditable(false);

        // Sample report data - you will call the report generation method here
        reportTextArea.setText(report);
        // Print button logic
        printButton.setOnAction(e -> {
            // Print the content of reportTextArea
            String contentToPrint = reportTextArea.getText();
            if (!contentToPrint.isEmpty()) {
                print(contentToPrint);  // You need to implement this print function
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No report to print!");
                alert.show();
            }
        });

        // Button to close the report scene
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            stage.close();  // Close the report scene
        });

        // Add components to layout
        layout.getChildren().addAll(reportTextArea, printButton, closeButton);

        // Create and show the scene
        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Generate Report");
        stage.show();
    }
    private static void print(String content) {
        // Create a PrinterJob
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        if (printerJob == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No printers found. Please check your printer setup.");
            alert.show();
            return;
        }

        // Prepare the content to print
        Text printableContent = new Text(content);
        printableContent.setWrappingWidth(500); // Set the width for the printable text if needed

        // Show print dialog to the user
        boolean proceed = printerJob.showPrintDialog(null); // Pass a stage here if available

        if (proceed) {
            // Print the content
            boolean success = printerJob.printPage(printableContent);

            if (success) {
                printerJob.endJob();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Printing complete.");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to print.");
                alert.show();
            }
        } else {
            // User cancelled the print dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Printing cancelled.");
            alert.show();
        }
    }
    public static void buyerDashboard(Stage stage){
        VBox vBox = new VBox();
        Button viewPlots = new Button("View Available Plots");
        Button requestPlot = new Button("Request Plot");
        Button ownershipDetails = new Button("Ownership Details");
        Button trackPaymentStatus = new Button("Track Payment Status");
        Button updatePreference = new Button("Update Preference");
        Button exit = new Button("Exit");
        Button logout = new Button("Logout");
        viewPlots.setOnAction(e->{
            viewPlots(stage);
        });
        requestPlot.setOnAction(e->{
            requestPlot(stage);
        });
        ownershipDetails.setOnAction(e->{
            ownershipDetails(stage);
        });
        trackPaymentStatus.setOnAction(e->{
            trackPaymentStatus(stage);
        });
        updatePreference.setOnAction(e->{
            updatePreference(stage);
        });
        logout.setOnAction(e->{
            buyerDashboard(stage);
        });
        exit.setOnAction(e->{
            stage.close();
        });
        vBox.getChildren().addAll(viewPlots,requestPlot,ownershipDetails,trackPaymentStatus,updatePreference,exit, logout);
        Scene scene = new Scene(vBox, 800, 800);
        stage.setScene(scene);
        stage.setTitle("Buyer Dashboard");
    }
    public static void adminLoginScreen(Stage stage){
        VBox vBox = new VBox();
        Label usernameLabel = new Label("Username: ");
        Label passwordLabel = new Label("Password: ");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        vBox.getChildren().addAll(usernameLabel,usernameField,passwordLabel,passwordField);
        User userObj = new User();
        List<User> users = userObj.loadUsers();
        Button login = new Button("Login");
        login.setOnAction(e->{
            if(users!=null){
                for(User user: users){
                    if("Admin".equals(usernameField.getText())&&"admin".equals(passwordField.getText())){
                        adminDashboardScene(stage);
                        break;
                    }
                    else{
                        Text invalid = new Text("Invalid Username or Password");
                        vBox.getChildren().add(invalid);
                    }
                }
            }

        });
        vBox.getChildren().add(login);
        Scene scene = new Scene(vBox, 800, 800);
        stage.setScene(scene);
        stage.setTitle("Login Screen");
    }
    public static void registerUser(Stage stage){
        VBox vBox = new VBox();
        Label username = new Label("Username: ");
        Label password = new Label("Password: ");
        Label email = new Label ("Email: ");
        Label phoneNumber = new Label("Phone Number: ");
        Label preferredLocation = new Label("Preferred Location: ");
        Label preferredSize = new Label("Preferred Size: ");
        Label budget = new Label("Budget: ");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField emailField = new TextField();
        TextField phoneNumberField = new TextField();
        TextField preferredLocationField = new TextField();
        TextField preferredSizeField = new TextField();
        TextField budgetField = new TextField();
        vBox.getChildren().addAll(username,usernameField,password, passwordField, email, emailField, phoneNumber, phoneNumberField, preferredLocation, preferredLocationField, preferredSize, preferredSizeField, budget, budgetField);
        Button login = new Button("Login");
        login.setOnAction(e->{
            List<User> users = loadUsers();
            List<Buyer> buyers = loadBuyers();
            Buyer buyer = new Buyer(usernameField.getText(), passwordField.getText(), "Buyer", emailField.getText(), phoneNumberField.getText(), preferredLocationField.getText(),Double.parseDouble(preferredSizeField.getText()),Double.parseDouble(budgetField.getText()));
            buyers.add(buyer);
            users.add(buyer);
            saveBuyers((ObservableList<Buyer>) buyers);
            saveUsers((ObservableList<User>) users);
            buyerDashboard(stage);
        });
        vBox.getChildren().add(login);
        Scene scene = new Scene(vBox, 800, 800);
        stage.setScene(scene);
        stage.setTitle("Login Screen");
    }

    private static void viewPlots(Stage stage){
        VBox vBox = new VBox();
        Button exit = new Button("Exit");
        TableView plotTable = new TableView<>();
        ObservableList<Plot> plots = loadPlots();

        // Define columns
        TableColumn<Plot, Integer> plotIdColumn = new TableColumn<>("Plot ID");
        plotIdColumn.setCellValueFactory(new PropertyValueFactory<>("plotId"));

        TableColumn<Plot, String> plotNumberColumn = new TableColumn<>("Plot Number");
        plotNumberColumn.setCellValueFactory(new PropertyValueFactory<>("plotNumber"));

        TableColumn<Plot, Double> lengthColumn = new TableColumn<>("Length");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn<Plot, Double> widthColumn = new TableColumn<>("Width");
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));

        TableColumn<Plot, Double> totalAreaColumn = new TableColumn<>("Total Area");
        totalAreaColumn.setCellValueFactory(new PropertyValueFactory<>("totalArea"));

        TableColumn<Plot, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Plot, String> gpsCoordinatesColumn = new TableColumn<>("GPS Coordinates");
        gpsCoordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("gpsCoordinates"));

        TableColumn<Plot, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Plot, Double> pricePerUnitColumn = new TableColumn<>("Price per Unit");
        pricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));

        TableColumn<Plot, Double> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        TableColumn<Plot, String> developmentStatusColumn = new TableColumn<>("Development Status");
        developmentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("developmentStatus"));

        // Add columns to TableView
        plotTable.getColumns().addAll(
                plotIdColumn, plotNumberColumn, lengthColumn, widthColumn, totalAreaColumn,
                locationColumn, gpsCoordinatesColumn, statusColumn, pricePerUnitColumn,
                totalPriceColumn, developmentStatusColumn
        );

        // Add sample data
        ObservableList<Plot> plotData = FXCollections.observableArrayList(
                new Plot(1, "P001", 50.0, 30.0, "Sector A", "27.2046,77.4977", "Available", 2000.0,  "Developed"),
                new Plot(2, "P002", 60.0, 40.0, "Sector B", "28.7041,77.1025", "Available", 2200.0,  "Under Development")
        );
        for(Plot plot: plotData){
            if(!(plot.getStatus().equals("Available")))
                plotData.remove(plot);
        }
        plotTable.setItems(plotData);
        exit.setOnAction(e->{
            buyerDashboard(stage);
        });
        vBox.getChildren().addAll(plotTable,exit);
        Scene scene = new Scene(vBox, 800, 800);
        stage.setScene(scene);
        stage.setTitle("Login Screen");
    }
    public static void requestPlot(Stage stage) {
        VBox vBox = new VBox();
        Label label = new Label("Request a Plot");
        TextField plotIdField = new TextField();
        plotIdField.setPromptText("Enter Plot ID");
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> {
            // Placeholder for request plot logic
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Plot request submitted!");
            alert.showAndWait();
            ObservableList<Plot> plots = loadPlots();
            for (Plot plot : plots) {
                if (plot.getPlotId()==Integer.parseInt(plotIdField.getText())) {
                    plot.setStatus("Reserved");
                    break;
                }
            }
            savePlots(plots);
        });

        backButton.setOnAction(e -> {
            buyerDashboard(stage);
        });

        vBox.getChildren().addAll(label, plotIdField, submitButton, backButton);
        Scene scene = new Scene(vBox, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Request Plot");
    }

    public static void ownershipDetails(Stage stage) {
        VBox vBox = new VBox();
        Label label = new Label("Ownership Details");
        TableView<Document> tableView = new TableView<>();

        // Define columns for the TableView
        TableColumn<Document, Integer> documentIdColumn = new TableColumn<>("Document ID");
        documentIdColumn.setCellValueFactory(new PropertyValueFactory<>("documentId"));

        TableColumn<Document, Integer> buyerIdColumn = new TableColumn<>("Buyer ID");
        buyerIdColumn.setCellValueFactory(new PropertyValueFactory<>("buyerId"));

        TableColumn<Document, Integer> plotIdColumn = new TableColumn<>("Plot ID");
        plotIdColumn.setCellValueFactory(new PropertyValueFactory<>("plotId"));

        TableColumn<Document, String> documentTypeColumn = new TableColumn<>("Document Type");
        documentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("documentType"));

        TableColumn<Document, LocalDate> uploadDateColumn = new TableColumn<>("Upload Date");
        uploadDateColumn.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));

        // Add columns to the TableView
        tableView.getColumns().addAll(documentIdColumn, buyerIdColumn, plotIdColumn, documentTypeColumn, uploadDateColumn);

        // Load ownership documents and set them in the TableView
        List<Document> documents = loadDocuments();
        ObservableList<Document> ownershipDocs = FXCollections.observableArrayList();

        for (Document doc : documents) {
            if ("Ownership".equalsIgnoreCase(doc.getDocumentType())) {
                ownershipDocs.add(doc);
            }
        }

        tableView.setItems(ownershipDocs);

        // Back button to return to buyerDashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> buyerDashboard(stage));

        vBox.getChildren().addAll(label, tableView, backButton);
        Scene scene = new Scene(vBox, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Ownership Details");
    }

    public static void trackPaymentStatus(Stage stage) {
        VBox vBox = new VBox();
        Label label = new Label("Track Payment Status");

        // Input fields for plot ID (to track payments)
        TextField plotIdField = new TextField();
        plotIdField.setPromptText("Enter Plot ID");

        Button searchButton = new Button("Search Payments");

        // TableView for displaying payment history
        TableView<Payment> tableView = new TableView<>();

        // Define columns for the TableView
        TableColumn<Payment, Integer> paymentIdColumn = new TableColumn<>("Payment ID");
        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("paymentId"));

        TableColumn<Payment, Integer> buyerIdColumn = new TableColumn<>("Buyer ID");
        buyerIdColumn.setCellValueFactory(new PropertyValueFactory<>("buyerId"));

        TableColumn<Payment, Integer> plotIdColumn = new TableColumn<>("Plot ID");
        plotIdColumn.setCellValueFactory(new PropertyValueFactory<>("plotId"));

        TableColumn<Payment, Double> amountPaidColumn = new TableColumn<>("Amount Paid");
        amountPaidColumn.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));

        TableColumn<Payment, Double> outstandingBalanceColumn = new TableColumn<>("Outstanding Balance");
        outstandingBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("outstandingBalance"));

        TableColumn<Payment, String> paymentMethodColumn = new TableColumn<>("Payment Method");
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        TableColumn<Payment, LocalDate> paymentDateColumn = new TableColumn<>("Payment Date");
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        // Add columns to the TableView
        tableView.getColumns().addAll(paymentIdColumn, buyerIdColumn, plotIdColumn, amountPaidColumn, outstandingBalanceColumn, paymentMethodColumn, paymentDateColumn);

        // Action for search button
        searchButton.setOnAction(e -> {
            String plotIdText = plotIdField.getText();
            if (plotIdText.isEmpty()) {
                showAlert("Error", "Please enter a Plot ID.");
                return;
            }

            try {
                int plotId = Integer.parseInt(plotIdText);

                // Fetch payment history for the entered plot ID
                List<Payment> paymentList = new ArrayList<>();
                List<Payment> payments = loadPayments();
                for(Payment payment: payments){
                    if(payment.getPlotId()==plotId){
                        paymentList.add(payment);
                    }
                }

                if (paymentList.isEmpty()) {
                    showAlert("No Payments Found", "No payment records found for the given Plot ID.");
                } else {
                    ObservableList<Payment> paymentData = FXCollections.observableArrayList(paymentList);
                    tableView.setItems(paymentData);
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid Plot ID.");
            }
        });

        // Back button to return to buyerDashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> buyerDashboard(stage));

        vBox.getChildren().addAll(label, plotIdField, searchButton, tableView, backButton);
        Scene scene = new Scene(vBox, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Track Payment Status");
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void updatePreference(Stage stage) {
        VBox vBox = new VBox();
        Label label = new Label("Update Preferences");

        // Fields to input preferred location, size, and budget
        TextField preferredLocationField = new TextField();
        preferredLocationField.setPromptText("Enter preferred location");

        TextField preferredSizeField = new TextField();
        preferredSizeField.setPromptText("Enter preferred size (in square meters)");

        TextField budgetField = new TextField();
        budgetField.setPromptText("Enter budget");

        Button updateButton = new Button("Update");
        Button backButton = new Button("Back");

        updateButton.setOnAction(e -> {
            // Retrieve the input values
            String preferredLocation = preferredLocationField.getText();
            String preferredSizeText = preferredSizeField.getText();
            String budgetText = budgetField.getText();

            // Validate the inputs
            if (preferredLocation.isEmpty() || preferredSizeText.isEmpty() || budgetText.isEmpty()) {
                showAlert("Error", "Please fill all the fields.");
                return;
            }

            double preferredSize = 0;
            double budget = 0;

            try {
                preferredSize = Double.parseDouble(preferredSizeText);
                budget = Double.parseDouble(budgetText);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter valid numbers for size and budget.");
                return;
            }

            // Placeholder: Logic for updating preferences (e.g., storing them in a database)
            // For now, just show a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Preferences updated successfully!");
            alert.showAndWait();

            // After updating, go back to the buyer dashboard
            buyerDashboard(stage);
        });

        backButton.setOnAction(e -> {
            buyerDashboard(stage);
        });

        vBox.getChildren().addAll(label, preferredLocationField, preferredSizeField, budgetField, updateButton, backButton);
        Scene scene = new Scene(vBox, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Update Preferences");
    }
    // Load Users as ObservableList
    public static ObservableList<User> loadUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Users.ser"))) {
            List<User> userList = (List<User>) inputStream.readObject();
            users.addAll(userList); // Add all items to ObservableList
            userCount = users.size() + 1; // Assuming `userCount` is declared elsewhere.
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Save Users
    public static void saveUsers(ObservableList<User> users) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Users.ser"))) {
            outputStream.writeObject(new ArrayList<>(users)); // Convert ObservableList to ArrayList
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load Buyers as ObservableList
    public static ObservableList<Buyer> loadBuyers() {
        ObservableList<Buyer> buyers = FXCollections.observableArrayList();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Buyers.ser"))) {
            List<Buyer> buyerList = (List<Buyer>) inputStream.readObject();
            buyers.addAll(buyerList);
        } catch (FileNotFoundException e) {
            System.out.println("Buyers file not found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return buyers;
    }

    // Save Buyers
    public static void saveBuyers(ObservableList<Buyer> buyers) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Buyers.ser"))) {
            outputStream.writeObject(new ArrayList<>(buyers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load Plots as ObservableList
    public static ObservableList<Plot> loadPlots() {
        ObservableList<Plot> plots = FXCollections.observableArrayList();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Plots.ser"))) {
            List<Plot> plotList = (List<Plot>) inputStream.readObject();
            plots.addAll(plotList);
        } catch (FileNotFoundException e) {
            System.out.println("Plots file not found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return plots;
    }

    // Save Plots
    public static void savePlots(ObservableList<Plot> plots) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Plots.ser"))) {
            outputStream.writeObject(new ArrayList<>(plots));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load Payments as ObservableList
    public static ObservableList<Payment> loadPayments() {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Payments.ser"))) {
            List<Payment> paymentList = (List<Payment>) inputStream.readObject();
            payments.addAll(paymentList);
        } catch (FileNotFoundException e) {
            System.out.println("Payments file not found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // Save Payments
    public static void savePayments(ObservableList<Payment> payments) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Payments.ser"))) {
            outputStream.writeObject(new ArrayList<>(payments));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load Documents as ObservableList
    public static ObservableList<Document> loadDocuments() {
        ObservableList<Document> documents = FXCollections.observableArrayList();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Documents.ser"))) {
            List<Document> documentList = (List<Document>) inputStream.readObject();
            documents.addAll(documentList);
        } catch (FileNotFoundException e) {
            System.out.println("Documents file not found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return documents;
    }

    // Save Documents
    public static void saveDocuments(ObservableList<Document> documents) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Documents.ser"))) {
            outputStream.writeObject(new ArrayList<>(documents));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}