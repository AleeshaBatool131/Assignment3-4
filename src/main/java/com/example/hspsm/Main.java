package com.example.hspsm;
import java.time.LocalDate;
import java.util.*;

public class Main {
        public static void main(String[] args) {
            // Admin initializes the system
            Admin admin = new Admin("adminUser", "admin123", "admin@example.com", "1234567890");

            // Buyers register in the system
            Buyer buyer1 = new Buyer( "buyerUser1", "buyer123", "buyer1@example.com", "0987654321", "Karachi", "Block-A",10.0, 10000000.0);
            Buyer buyer2 = new Buyer("buyerUser2", "buyer123", "buyer2@example.com", "0987654322", "Lahore", "Block-A",5.0, 5000000.0);

            // Buyers login
            buyer1.loginUser();
            buyer2.loginUser();

            // Admin adds new plots to the system
            Plot plot1 = new Plot(101, "A-1", 40, 50, "DHA Phase 6, Block A", "24.8607° N, 67.0011° E", "Available", 5000, "Developed");
            Plot plot2 = new Plot(102, "A-2", 50, 60, "DHA Phase 6, Block B", "24.8610° N, 67.0020° E", "Available", 5500, "Undeveloped");

            admin.addPlot(plot1);
            admin.addPlot(plot2);

            // Admin views all plots
            System.out.println("Plot Details:");
            plot1.getPlotDetails();
            plot2.getPlotDetails();

            // Calculate area of a plot
            double plot1Area = plot1.calculateArea();
            System.out.println("Plot A-1 Area: " + plot1Area + " sq ft");

            // Buyers view available plots
            List<Plot> availablePlots = buyer1.viewAvailablePlots();
            System.out.println("Available Plots:");
            for (Plot plot : availablePlots) {
            plot.getPlotDetails();
            }

            // Buyer reserves a plot
            buyer1.requestPlot(plot1.getPlotId());
            Reservation reservation1 = new Reservation(1, plot1.getPlotId(), buyer1.getBuyerId(), LocalDate.now(), "Reserved");
            System.out.println("Reservation Details: ");
            reservation1.getReservationDetails();

            // Admin updates the plot status
            admin.updatePlotDetails(plot1);
            System.out.println("Plot A-1 status updated to Reserved.");

            // Admin generates a sales report
            String salesReport = admin.generateReports();
            System.out.println("Sales Report:\n" + salesReport);

            // Buyer makes a payment
            Payment payment1 = new Payment( plot1.getPlotId(), buyer1.getBuyerId(), 2000000, "Bank Transfer");
            payment1.recordPayment();
            System.out.println("Payment recorded for Buyer 1.");

            // Buyer checks payment history and outstanding balance
            List<Payment> paymentHistory = payment1.trackPaymentHistory(plot1.getPlotId());
            System.out.println("Payment History for Plot A-1:");
            for (Payment payment : paymentHistory) {
                System.out.println("Paid: " + payment.getAmountPaid() + " on " + payment.getPaymentDate());
            }

            double outstandingBalance = payment1.getOutstandingBalance(plot1.getPlotId());
            System.out.println("Outstanding Balance: " + outstandingBalance);

            // Admin uploads ownership documents for the buyer
            Document ownershipDoc = new Document(1, plot1.getPlotId(), buyer1.getBuyerId(), "Ownership Certificate", LocalDate.now());
            ownershipDoc.uploadDocument();
            System.out.println("Ownership Document uploaded for Plot A-1.");

            // Buyer views ownership details
            List<String> ownershipDetails = buyer1.getOwnershipDetails();
            System.out.println("Ownership Details:");
            for (String detail : ownershipDetails) {
                System.out.println(detail);
            }

            // Admin sends notifications
            Notification notification = new Notification(1, buyer1.getBuyerId(), "Your reservation for Plot A-1 is confirmed.", "Unread");
            notification.sendNotification(buyer1.getBuyerId(), "Your reservation for Plot A-1 is confirmed.");
            System.out.println("Notification sent to Buyer 1.");

            // Buyer retrieves and reads notifications
            List<Notification> notifications = notification.getNotifications(buyer1.getBuyerId());
            System.out.println("Notifications for Buyer 1:");
            for (Notification n : notifications) {
                System.out.println(n.getMessage());
                n.markAsRead(n.getNotificationId());
            }

            // Analyze plot statistics (admin functionality)
            String plotStatistics = admin.analyzePlotStatistics();
            System.out.println("Plot Statistics:\n" + plotStatistics);

            // Map functionality
            Map map = new Map();
            map.loadMap();
            map.markPlot(plot1.getPlotId(), "Reserved");
            Plot clickedPlot = map.getPlotDetailsFromMap("DHA Phase 6, Block A");
            System.out.println("Clicked Plot Details:\n" );
            clickedPlot.getPlotDetails();

            // End of simulation
            System.out.println("System simulation completed successfully.");
        }
    }
