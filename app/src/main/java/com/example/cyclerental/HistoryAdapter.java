package com.example.cyclerental;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<History> historyList;

    public HistoryAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = historyList.get(position);

        holder.tvCycleName.setText(history.getCycleName());
        holder.tvCycleType.setText(history.getCycleType());
        holder.tvCycleLocation.setText(history.getCycleLocation());
        holder.tvRentalDate.setText(history.getRentalDate());
        holder.tvRentalTime.setText(history.getRentalTime());
        holder.tvPaymentStatus.setText(history.getPaymentStatus());
        holder.tvRentalPlan.setText(history.getRentalPlan());

        if (history.isOngoing()) {
            holder.tvStatus.setText("Ongoing");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
            holder.tvRemainingTime.setVisibility(View.VISIBLE);
            holder.tvRemainingTime.setText(history.getRemainingTime());
        } else {
            holder.tvStatus.setText("Completed");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray));
            holder.tvRemainingTime.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCycleName, tvCycleType, tvCycleLocation, tvRentalDate,
                tvRentalTime, tvPaymentStatus, tvRentalPlan, tvStatus, tvRemainingTime;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCycleName = itemView.findViewById(R.id.tvCycleName);
            tvCycleType = itemView.findViewById(R.id.tvCycleType);
            tvCycleLocation = itemView.findViewById(R.id.tvCycleLocation);
            tvRentalDate = itemView.findViewById(R.id.tvRentalDate);
            tvRentalTime = itemView.findViewById(R.id.tvRentalTime);
            tvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
            tvRentalPlan = itemView.findViewById(R.id.tvRentalPlan);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvRemainingTime = itemView.findViewById(R.id.tvRemainingTime);
        }
    }
}