package com.example.cyclerental;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CycleAdapter extends RecyclerView.Adapter<CycleAdapter.CycleViewHolder> {
    private List<Cycle> cycleList;
    private Context context;

    public CycleAdapter(Context context, List<Cycle> cycleList) {
        this.context = context;
        this.cycleList = cycleList;
    }



    @NonNull
    @Override
    public CycleAdapter.CycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cycle, parent, false);
        return new CycleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CycleAdapter.CycleViewHolder holder, int position) {
        Cycle cycle = cycleList.get(position);

        holder.tvSName.setText(cycle.getCname());
        String formattedPrice = "$" + cycle.getCprice();
        holder.tvSPrice.setText("Price: " + formattedPrice);

        holder.tvSType.setText("Type: " + cycle.getType());
        holder.tvSLocation.setText("Location: " + cycle.getLocation());
        holder.tvSAvailability.setText(cycle.getAvailability() == 1 ? "Available" : "Not Available");


        String imageName = cycle.getCimagePath();


        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());


        if (imageResId != 0) {
            holder.ivCycle.setImageResource(imageResId);
        } else {
            holder.ivCycle.setImageResource(R.drawable.placeholder_image);
        }

        holder.btnRent.setOnClickListener(v -> {
            Intent intent = new Intent(context, RentActivity.class);
            intent.putExtra("cycle_name", cycle.getCname());

            intent.putExtra("cycle_price", formattedPrice);

            intent.putExtra("cycle_type", cycle.getType());
            intent.putExtra("cycle_location", cycle.getLocation());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return cycleList.size();
    }

    public void updateList(List<Cycle> newList) {
        cycleList = newList;
        notifyDataSetChanged();
    }


    public static class CycleViewHolder extends RecyclerView.ViewHolder{
        TextView tvSName, tvSPrice, tvSType, tvSLocation, tvSAvailability;
        ImageView ivCycle;
        Button btnRent;

        public CycleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSName = itemView.findViewById(R.id.tvSName);
            tvSPrice = itemView.findViewById(R.id.tvSPrice);
            tvSType = itemView.findViewById(R.id.tvSType);
            tvSLocation = itemView.findViewById(R.id.tvSLocation);
            tvSAvailability = itemView.findViewById(R.id.tvSAvailability);
            ivCycle = itemView.findViewById(R.id.ivCycle);
            btnRent = itemView.findViewById(R.id.btnRent);
        }

    }
}