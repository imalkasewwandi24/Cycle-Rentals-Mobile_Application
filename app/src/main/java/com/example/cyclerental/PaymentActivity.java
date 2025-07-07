package com.example.cyclerental;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvPPrice;
    private EditText etPCardNumber, etPCardHolder, etPDate, etPCvv;
    private CheckBox cbPTerms;
    private Button btnPPayNow;
    private DBHelper dbHelper;
    private int rentalId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dbHelper = new DBHelper(this);

        tvPPrice = findViewById(R.id.tvPPrice);
        etPCardNumber = findViewById(R.id.etPCardNumber);
        etPCardHolder = findViewById(R.id.etPCardHolder);
        etPDate = findViewById(R.id.etPDate);
        etPCvv = findViewById(R.id.etPCvv);
        cbPTerms = findViewById(R.id.cbPTerms);
        btnPPayNow = findViewById(R.id.btnPPayNow);

        double totalPrice = getIntent().getDoubleExtra("total_price", 0.0);
        rentalId = getIntent().getIntExtra("rental_id", -1);
        tvPPrice.setText("Total Payable : $" + String.format("%.2f", totalPrice));

        btnPPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment(totalPrice);
            }
        });
    }

    private void processPayment(double totalPrice) {
        String cardNumber = etPCardNumber.getText().toString().trim();
        String cardHolder = etPCardHolder.getText().toString().trim();
        String expiryDate = etPDate.getText().toString().trim();
        String cvv = etPCvv.getText().toString().trim();
        boolean saveCardDetails = cbPTerms.isChecked();

        if (cardNumber.isEmpty() || cardHolder.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(PaymentActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cardNumber.matches("\\d{16}")) {
            Toast.makeText(PaymentActivity.this, "Please enter a valid 16-digit card number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cvv.matches("\\d{3}")) {
            Toast.makeText(PaymentActivity.this, "Please enter a valid 3-digit CVV", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            Toast.makeText(PaymentActivity.this, "Please enter a valid expiry date (MM/YY)", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yy", Locale.getDefault());
            Date expiry = sdf.parse(expiryDate);
            Date currentDate = new Date();
            if (expiry != null && expiry.before(currentDate)) {
                Toast.makeText(PaymentActivity.this, "Your card has expired", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            Toast.makeText(PaymentActivity.this, "Invalid expiry date format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (saveCardDetails) {
            // Logic to save card details
        }

        boolean isUpdated = dbHelper.updatePaymentStatus(rentalId, "Paid");
        if (isUpdated) {
            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PaymentActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Payment Update Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}

