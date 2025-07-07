package com.example.cyclerental;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class RentActivity extends AppCompatActivity {

    private TextView tvRentName, tvRentPrice, tvRentType, tvRentLocation;
    private TextView tvPickupDate, tvPickupTime, tvRentalDuration;
    private Spinner spPricingPlan, spRentalDuration, spReturnLocation;
    private Button btnConfirmRent, btnPayment;
    private ImageView ivRenterImg;
    private DBHelper dbHelper;
    private int rentalId = -1;

    private String selectedPlan = "Pay As You Go";
    private String selectedDuration = "1 hour";
    private String selectedReturnLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        dbHelper = new DBHelper(this);

        // Initialize views
        tvRentName = findViewById(R.id.tvRentName);
        tvRentPrice = findViewById(R.id.tvRentPrice);
        tvRentType = findViewById(R.id.tvRentType);
        tvRentLocation = findViewById(R.id.tvRentLocation);
        tvPickupDate = findViewById(R.id.tvPickupDate);
        tvPickupTime = findViewById(R.id.tvPickupTime);
        tvRentalDuration = findViewById(R.id.tvRentalDuration);
        spPricingPlan = findViewById(R.id.spPricingPlan);
        spRentalDuration = findViewById(R.id.spRentalDuration);
        spReturnLocation = findViewById(R.id.spReturnLocation);
        btnConfirmRent = findViewById(R.id.btnConfirmRent);
        btnPayment = findViewById(R.id.btnPayment);
        ivRenterImg = findViewById(R.id.ivRenterImg);
        ivRenterImg.setImageResource(R.drawable.bicycle2);

        // Retrieve intent data
        Intent intent = getIntent();
        if (intent != null) {
            String cycleName = intent.getStringExtra("cycle_name");
            String cyclePrice = intent.getStringExtra("cycle_price");
            String cycleType = intent.getStringExtra("cycle_type");
            String cycleLocation = intent.getStringExtra("cycle_location");

            tvRentName.setText("Cycle Name: " + cycleName);
            tvRentPrice.setText("Price(1hr): " + cyclePrice);
            tvRentType.setText("Type: " + cycleType);
            tvRentLocation.setText("Location: " + cycleLocation);
        }
        // Retrieve email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", ""); // Default value is an empty string if email doesn't exist

        // Set up Pricing Plan Spinner
        String[] pricingPlans = {"Pay As You Go", "Daily Ride", "Weekly Ride", "Monthly Ride", "Uni Go"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pricingPlans);
        spPricingPlan.setAdapter(planAdapter);
        spPricingPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPlan = pricingPlans[position];
                updateRentalDurationVisibility();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set up Rental Duration Spinner (Initially Hidden)
        String[] rentalDurations = {
                "1 hour", "2 hours", "3 hours", "4 hours", "5 hours",
                "6 hours", "7 hours", "8 hours", "9 hours", "10 hours"
        };
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rentalDurations);
        spRentalDuration.setAdapter(durationAdapter);
        spRentalDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDuration = rentalDurations[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set up Return Location Spinner
        String[] returnLocations = {"Texas", "Florida", "New York", "Arizona"};
        ArrayAdapter<String> returnAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, returnLocations);
        spReturnLocation.setAdapter(returnAdapter);
        spReturnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedReturnLocation = returnLocations[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Date Picker
        tvPickupDate.setOnClickListener(v -> showDatePicker());

        // Time Picker
        tvPickupTime.setOnClickListener(v -> showTimePicker());

        // Confirm Rent Button
        btnConfirmRent.setOnClickListener(v -> confirmRental());

        // Proceed to Payment Button
        btnPayment.setOnClickListener(v -> {
            if (rentalId == -1) {
                Toast.makeText(RentActivity.this, "Error: Rental not confirmed", Toast.LENGTH_SHORT).show();
                return;
            }
            double totalPrice = calculateTotalPrice();
            Intent intentToPayment = new Intent(RentActivity.this, PaymentActivity.class);
            intentToPayment.putExtra("total_price", totalPrice);
            intentToPayment.putExtra("rental_id", rentalId);
            startActivity(intentToPayment);
        });




    }

    // Show Date Picker Dialog
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            tvPickupDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    // Show Time Picker Dialog
    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String selectedTime = hourOfDay + ":" + (minute1 < 10 ? "0" + minute1 : minute1);
            tvPickupTime.setText(selectedTime);
        }, hour, minute, true);
        timePickerDialog.show();
    }

    // Update Rental Duration Visibility
    private void updateRentalDurationVisibility() {
        if (selectedPlan.equals("Pay As You Go")) {
            tvRentalDuration.setVisibility(View.VISIBLE);
            spRentalDuration.setVisibility(View.VISIBLE);
        } else {
            tvRentalDuration.setVisibility(View.GONE);
            spRentalDuration.setVisibility(View.GONE);
        }
    }

    // Confirm Rental Logic
    private void confirmRental() {
        String pickupDate = tvPickupDate.getText().toString();
        String pickupTime = tvPickupTime.getText().toString();

        if (pickupDate.equals("Select Date") || pickupTime.equals("Select Time")) {
            Toast.makeText(this, "Please select Pickup Date & Time", Toast.LENGTH_SHORT).show();
            return;
        }

        String cycleName = tvRentName.getText().toString().replace("Cycle Name: ", "");
        String cyclePriceText = tvRentPrice.getText().toString().replace("Price(1hr): $", "").replace("$", "").trim();
        double cyclePrice = 0;
        try {
            cyclePrice = Double.parseDouble(cyclePriceText);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String cycleType = tvRentType.getText().toString().replace("Type: ", "");
        String cycleLocation = tvRentLocation.getText().toString().replace("Location: ", "");

        // Apply rental duration based on the selected plan
        String rentalDuration;
        switch (selectedPlan) {
            case "Daily Ride":
                rentalDuration = "24 hours";
                break;
            case "Weekly Ride":
                rentalDuration = "168 hours";
                break;
            case "Monthly Ride":
                rentalDuration = "730 hours";
                break;
            case "Uni Go":
                rentalDuration = "720 hours";
                break;
            default: // Pay As You Go
                rentalDuration = selectedDuration;
                break;
        }

        // Retrieve the email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");


        rentalId = dbHelper.insertRentalAndReturnId(
                tvRentName.getText().toString().replace("Cycle Name: ", ""),
                Double.parseDouble(tvRentPrice.getText().toString().replace("Price(1hr): $", "").trim()),
                tvRentType.getText().toString().replace("Type: ", ""),
                tvRentLocation.getText().toString().replace("Location: ", ""),
                tvPickupDate.getText().toString(),
                tvPickupTime.getText().toString(),
                selectedPlan,
                selectedDuration, // Ensure the correct rental duration is passed
                selectedReturnLocation,
                "Pending", // Payment status starts as "Pending"
                userEmail
        );

        if (rentalId != -1) {
            Toast.makeText(this, "Rental Confirmed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error confirming rental!", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateTotalPrice() {
        double cyclePrice = Double.parseDouble(tvRentPrice.getText().toString().replace("Price(1hr): $", "").trim());
        double totalPrice = 0;

        switch (selectedPlan) {
            case "Pay As You Go":
                int hours = Integer.parseInt(selectedDuration.split(" ")[0]);
                double planPrice = 2.0;
                totalPrice = planPrice * cyclePrice * hours;
                break;
            case "Daily Ride":
                double dailyPrice = cyclePrice * 24;
                totalPrice = dailyPrice - (dailyPrice * 10 / 100);
                break;
            case "Weekly Ride":
                double weeklyPrice = cyclePrice * 168;
                totalPrice = weeklyPrice - (weeklyPrice * 15 / 100);
                break;
            case "Monthly Ride":
                double monthlyPrice = cyclePrice * 730;
                totalPrice = monthlyPrice - (monthlyPrice * 30/100);
                break;
            case "Uni Go":
                double uniPrice = cyclePrice * 720;
                totalPrice = uniPrice - (uniPrice * 50/100);
                break;
        }
        return totalPrice;
    }

}