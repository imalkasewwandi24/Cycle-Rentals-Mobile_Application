package com.example.cyclerental;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private HistoryAdapter historyAdapter;
    private List<History> historyList = new ArrayList<>();
    private DBHelper dbHelper;
    private Handler handler = new Handler();
    private Runnable updateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DBHelper(this);
        rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        loadHistoryData();

        // Set up periodic updates for countdown
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (historyAdapter != null) {
                    historyAdapter.notifyDataSetChanged();
                }
                handler.postDelayed(this, 60000); // Update every minute
            }
        };
    }

    private void loadHistoryData() {
        // Get user email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        // Get history data from DB
        List<History> dbHistory = dbHelper.getRentalHistory(userEmail);

        if (dbHistory != null) {
            historyList.clear();
            historyList.addAll(dbHistory);

            if (historyAdapter == null) {
                historyAdapter = new HistoryAdapter(historyList);
                rvHistory.setAdapter(historyAdapter);
            } else {
                historyAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start periodic updates when activity is visible
        handler.post(updateRunnable);
        // Refresh data in case something changed
        loadHistoryData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop updates when activity is not visible
        handler.removeCallbacks(updateRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateRunnable);
    }
}