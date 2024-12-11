package com.example.hspsm;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;
import javafx.scene.*;

import java.io.*;

public class HelloApplication extends Application{
    public static int userCount = 1;
    @Override
    public void start(Stage stage) throws IOException {
        welcomeScreen(stage);
        //adminDashboardScene(stage,root);
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
        try{
            Thread.sleep(200);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        loginScreen(stage);
    }
    public static void loginScreen(Stage stage){
        VBox vBox = new VBox();
        Label usernameLabel = new Label("Username: ");
        Label passwordLabel = new Label("Password: ");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button login = new Button("Login");
        List<User> users = loadUsers();
        if(users!=null)
        {
            for(User user: users){
                if(user.getUsername().equals(usernameField.getText())&&user.getPassword().equals(passwordField.getText())){
                    adminDashboardScene(stage);
                    break;
                }
                else{
                    Text invalid = new Text("Invalid Username or Password");
                    vBox.getChildren().add(invalid);
                }
            }
        }
        login.setOnAction(e->{
            buyerDashboard(stage);
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
        vBox.getChildren().addAll(login ,admin, register);
        Scene scene = new Scene(vBox, 800, 800);
        stage.setScene(scene);
        stage.setTitle("Login Screen");
    }

    public static void adminDashboardScene(Stage stage) {


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
            saveBuyers(buyers);
            saveUsers(users);
            buyerDashboard(stage);
        });
        vBox.getChildren().add(login);
        Scene scene = new Scene(vBox, 800, 800);
        stage.setScene(scene);
        stage.setTitle("Login Screen");
    }
    public static List<User> loadUsers() {
        List<User> users = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Users.ser"))) {
            users = (List<User>) inputStream.readObject();
            userCount=users.size()+1;
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found. Starting with an empty list.");
            users = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch(NullPointerException e){
            users = new ArrayList<>();
        }
        return users;
    }

    public static void saveUsers(List<User> users) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Users.ser"))) {
            outputStream.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<Buyer> loadBuyers() {
        List<Buyer> buyers = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Buyers.ser"))) {
            buyers = (List<Buyer>) inputStream.readObject();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException  e) {
            e.printStackTrace();
        }
        return buyers;
    }

    public static void saveBuyers(List<Buyer> buyers) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Buyers.ser"))) {
            outputStream.writeObject(buyers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static ObservableList<Plot> loadPlots() {
        ObservableList<Plot> plots = FXCollections.observableArrayList();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Plots.ser"))) {
            List<Plot> plotList = (List<Plot>) inputStream.readObject();
            plots.addAll(plotList);
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found. Starting with an empty list.");
        }
        catch (IOException | ClassNotFoundException| NullPointerException e) {
            e.printStackTrace();
        }
        return plots;
    }

    private static void savePlots(List<Plot> plots) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Plots.ser"))) {
            outputStream.writeObject(plots);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static List<Document> loadDocuments() {
        List<Document> documents = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Documents.ser"))) {
            documents = (List<Document>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Document file not found. Starting with an empty list.");
            documents = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public static void saveDocuments(List<Document> plots) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Documents.ser"))) {
            outputStream.writeObject(plots);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static List<Payment> loadPayments(){
        List<Payment> payments = null;
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Payments.ser"))){
            payments=(List<Payment>) inputStream.readObject();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return payments;
    }

    public static void savePayments(List<Payment> payments){
        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Payments.ser"))){
            outputStream.writeObject(payments);
        }
        catch (IOException e){
            e.printStackTrace();
        }
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


}