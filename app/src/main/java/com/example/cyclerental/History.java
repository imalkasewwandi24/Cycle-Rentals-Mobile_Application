package com.example.cyclerental;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class History {
    private String cycleName;
    private double cyclePrice;
    private String cycleType;
    private String cycleLocation;
    private String rentalTime;
    private String rentalDate;
    private String paymentStatus;
    private String rentalPlan;
    private String rentalDuration;
    private int rentalId;

    public History(String cycleName, double cyclePrice, String cycleType, String cycleLocation,
                   String rentalTime, String rentalDate, String paymentStatus,
                   String rentalPlan, String rentalDuration, int rentalId) {
        this.cycleName = cycleName;
        this.cyclePrice = cyclePrice;
        this.cycleType = cycleType;
        this.cycleLocation = cycleLocation;
        this.rentalTime = rentalTime;
        this.rentalDate = rentalDate;
        this.paymentStatus = paymentStatus;
        this.rentalPlan = rentalPlan;
        this.rentalDuration = rentalDuration;
        this.rentalId = rentalId;
    }

    // Getters
    public String getCycleName() { return cycleName; }
    public double getCyclePrice() { return cyclePrice; }
    public String getCycleType() { return cycleType; }
    public String getCycleLocation() { return cycleLocation; }
    public String getRentalTime() { return rentalTime; }
    public String getRentalDate() { return rentalDate; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getRentalPlan() { return rentalPlan; }
    public String getRentalDuration() { return rentalDuration; }
    public int getRentalId() { return rentalId; }

    // Method to check if rental is ongoing
    public boolean isOngoing() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            // Parse rental date and time
            String dateTimeStr = rentalDate + " " + rentalTime;
            Date startDate = sdf.parse(dateTimeStr);

            if (startDate == null) return false;

            // Calculate end time based on duration
            long durationHours = getDurationInHours();
            long endTimeMillis = startDate.getTime() + TimeUnit.HOURS.toMillis(durationHours);

            // Compare with current time
            return System.currentTimeMillis() < endTimeMillis;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get remaining time
    public String getRemainingTime() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String dateTimeStr = rentalDate + " " + rentalTime;
            Date startDate = sdf.parse(dateTimeStr);

            if (startDate == null) return "N/A";

            long durationHours = getDurationInHours();
            long endTimeMillis = startDate.getTime() + TimeUnit.HOURS.toMillis(durationHours);
            long remainingMillis = endTimeMillis - System.currentTimeMillis();

            if (remainingMillis <= 0) {
                return "Completed";
            }

            long hours = TimeUnit.MILLISECONDS.toHours(remainingMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60;

            return String.format(Locale.getDefault(), "%dh %02dm remaining", hours, minutes);
        } catch (ParseException e) {
            e.printStackTrace();
            return "N/A";
        }
    }

    private long getDurationInHours() {
        if (rentalPlan.equals("Daily Ride")) return 24;
        if (rentalPlan.equals("Weekly Ride")) return 168;
        if (rentalPlan.equals("Monthly Ride")) return 730;
        if (rentalPlan.equals("Uni Go")) return 720;

        // For "Pay As You Go" or others, parse from rentalDuration
        try {
            return Long.parseLong(rentalDuration.split(" ")[0]);
        } catch (Exception e) {
            return 1; // default to 1 hour if parsing fails
        }
    }
}